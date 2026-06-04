package com.smnotes.domain.sync

import android.util.Log
import com.smnotes.data.database.NoteDao
import com.smnotes.data.network.NetworkMonitor
import com.smnotes.data.remote.datasource.NoteRemoteDataSource
import com.smnotes.data.remote.dto.NoteRequestDto
import com.smnotes.data.remote.dto.NoteResponseDto
import com.smnotes.domain.model.Note
import com.smnotes.domain.model.SyncStatus
import java.time.Instant

class SyncManager(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource,
    private val networkMonitor: NetworkMonitor
) {

    suspend fun syncOnLogin() {
        if (!networkMonitor.isConnected()) return

        // Step 1: Mark all LOCAL → PENDING_UPLOAD so they get pushed below
        noteDao.markLocalAsPendingUpload()

        // Step 2: Fetch remote notes
        val remoteNotes = runCatching { noteRemoteDataSource.getAllNotes() }.getOrNull() ?: return
        val remoteIds = remoteNotes.map { it.id }.toSet()

        // Step 3: Merge remote into local using sync status as the conflict rule:
        //   - SYNCED → no pending local change, server is source of truth → accept server version
        //   - PENDING_UPLOAD / PENDING_DELETE → local change exists → keep local, push in Step 5
        remoteNotes.forEach { remote ->
            val existing = noteDao.getNoteByRemoteId(remote.id)
            if (existing == null) {
                noteDao.upsertNote(remote.toNote())
            } else if (existing.syncStatus == SyncStatus.SYNCED) {
                noteDao.upsertNote(
                    existing.copy(
                        title = remote.title,
                        content = remote.content,
                        color = remote.color,
                        isImportant = remote.isImportant,
                        syncStatus = SyncStatus.SYNCED
                    )
                )
            }
            // PENDING_UPLOAD or PENDING_DELETE: local change wins, Step 5 will push it
        }

        // Step 4: Delete SYNCED local notes whose remoteId is no longer on the server
        // (note was deleted on another device)
        noteDao.getNotesWithRemoteId()
            .filter { it.syncStatus == SyncStatus.SYNCED && it.remoteId !in remoteIds }
            .forEach { noteDao.deleteNote(it) }

        // Step 5: Push all pending changes
        syncPending()
    }

    suspend fun syncPending() {
        if (!networkMonitor.isConnected()) return

        // Push pending uploads
        noteDao.getPendingUpload().forEach { note ->
            runCatching {
                val dto = NoteRequestDto(
                    id = note.remoteId,
                    title = note.title,
                    content = note.content,
                    color = note.color,
                    isImportant = note.isImportant
                )
                val response = noteRemoteDataSource.saveNote(dto)
                noteDao.updateSyncStatus(note.id, response.id, SyncStatus.SYNCED)
            }.onFailure { Log.e("SyncManager", "Upload failed for note ${note.id}", it) }
        }

        // Push pending deletes
        noteDao.getPendingDelete().forEach { note ->
            Log.i("SyncManager", "Deleting note ${note.title} with remoteId ${note.remoteId}")
            runCatching {
                if (note.remoteId != null) {
                    noteRemoteDataSource.deleteNote(note.remoteId)
                }
                noteDao.deleteNote(note)
            }.onFailure { Log.e("SyncManager", "Delete failed for note ${note.id}", it) }
        }
    }

    // Pushes a single note by its local ID. Called immediately after the user saves a note
    // so we avoid scanning all PENDING_UPLOAD rows for a change that just happened.
    suspend fun syncNote(localId: Long) {
        if (!networkMonitor.isConnected()) return
        val note = noteDao.getNoteById(localId) ?: return
        if (note.syncStatus != SyncStatus.PENDING_UPLOAD) return
        runCatching {
            val dto = NoteRequestDto(
                id = note.remoteId,
                title = note.title,
                content = note.content,
                color = note.color,
                isImportant = note.isImportant
            )
            val response = noteRemoteDataSource.saveNote(dto)
            noteDao.updateSyncStatus(note.id, response.id, SyncStatus.SYNCED)
        }.onFailure { Log.e("SyncManager", "Upload failed for note $localId", it) }
    }

    // Pushes a single delete by local ID. Called immediately after the user deletes a note.
    suspend fun syncDeleteNote(localId: Long) {
        if (!networkMonitor.isConnected()) return
        val note = noteDao.getNoteById(localId) ?: return
        if (note.syncStatus != SyncStatus.PENDING_DELETE) return
        runCatching {
            if (note.remoteId != null) {
                noteRemoteDataSource.deleteNote(note.remoteId)
            }
            noteDao.deleteNote(note)
        }.onFailure { Log.e("SyncManager", "Delete failed for note $localId", it) }
    }

    private fun NoteResponseDto.toNote(): Note = Note(
        title = title,
        content = content,
        timestamp = Instant.parse(createdAt).toEpochMilli(),
        color = color,
        isImportant = isImportant,
        remoteId = id,
        syncStatus = SyncStatus.SYNCED
    )
}
