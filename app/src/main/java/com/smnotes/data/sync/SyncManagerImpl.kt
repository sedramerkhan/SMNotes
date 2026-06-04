package com.smnotes.data.sync

import android.util.Log
import com.smnotes.data.database.NoteDao
import com.smnotes.data.network.NetworkMonitor
import com.smnotes.data.remote.datasource.NoteRemoteDataSource
import com.smnotes.data.remote.dto.NoteRequestDto
import com.smnotes.data.remote.dto.NoteResponseDto
import com.smnotes.domain.model.Note
import com.smnotes.domain.model.SyncStatus
import com.smnotes.domain.sync.SyncManager
import java.time.Instant

class SyncManagerImpl(
    private val noteDao: NoteDao,
    private val noteRemoteDataSource: NoteRemoteDataSource,
    private val networkMonitor: NetworkMonitor
) : SyncManager {

    override suspend fun syncOnLogin() {
        if (!networkMonitor.isConnected()) return

        noteDao.markLocalAsPendingUpload()

        val remoteNotes = runCatching { noteRemoteDataSource.getAllNotes() }.getOrNull() ?: return
        val remoteIds = remoteNotes.map { it.id }.toSet()

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
        }

        noteDao.getNotesWithRemoteId()
            .filter { it.syncStatus == SyncStatus.SYNCED && it.remoteId !in remoteIds }
            .forEach { noteDao.deleteNote(it) }

        syncPending()
    }

    override suspend fun syncPending() {
        if (!networkMonitor.isConnected()) return

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

    override suspend fun syncNote(localId: Long) {
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

    override suspend fun syncDeleteNote(localId: Long) {
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
