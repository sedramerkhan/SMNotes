package com.smnotes.data.repository

import com.smnotes.data.database.NoteDao
import com.smnotes.data.network.NetworkMonitor
import com.smnotes.domain.model.Note
import com.smnotes.domain.model.SyncStatus
import com.smnotes.domain.repository.NoteRepository
import com.smnotes.domain.repository.SessionState
import com.smnotes.domain.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val sessionState: SessionState,
    private val networkMonitor: NetworkMonitor,
    private val syncManager: SyncManager,
    private val applicationScope: CoroutineScope
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> = dao.getNotes()

    override fun getImportantNotes(): Flow<List<Note>> = dao.getImportantNotes()

    override suspend fun getNoteById(id: Long): Note? = dao.getNoteById(id)

    override suspend fun insertNote(note: Note) {
        val existing = if (note.id != 0L) dao.getNoteById(note.id) else null
        val syncStatus = when {
            !sessionState.isLoggedIn() -> SyncStatus.LOCAL
            else -> SyncStatus.PENDING_UPLOAD
        }
        val localId = dao.insertNote(note.copy(syncStatus = syncStatus, remoteId = existing?.remoteId))
        if (sessionState.isLoggedIn() && networkMonitor.isConnected()) {
            applicationScope.launch { syncManager.syncNote(localId) }
        }
    }

    override suspend fun deleteNote(note: Note) {
        if (!sessionState.isLoggedIn()) {
            dao.deleteNote(note)
            return
        }
        // Use a targeted UPDATE (not a full REPLACE) so the existing remoteId in the DB
        // row is never overwritten. The UI-side note object can be stale (Compose captures
        // it at first composition in rememberDismissState), so passing it to dao.insertNote
        // would silently null-out the remoteId and break the backend DELETE.
        dao.markAsPendingDelete(note.id)
        if (networkMonitor.isConnected()) {
            applicationScope.launch { syncManager.syncDeleteNote(note.id) }
        }
    }
}
