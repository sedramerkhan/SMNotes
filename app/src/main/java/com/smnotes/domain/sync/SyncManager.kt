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

        // Step 3: Merge remote into local
        remoteNotes.forEach { remote ->
            val existing = noteDao.getNoteByRemoteId(remote.id)
            if (existing == null) {
                noteDao.insertNote(remote.toNote())
            } else {
                val remoteTimestamp = Instant.parse(remote.createdAt).toEpochMilli()
                if (remoteTimestamp > existing.timestamp) {
                    noteDao.insertNote(
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
            }
        }

        // Push pending deletes
        noteDao.getPendingDelete().forEach { note ->
            Log.i("SyncManager", "Deleting note ${note.title} with remoteId ${note.remoteId}")
            runCatching {
                if (note.remoteId != null) {
                    noteRemoteDataSource.deleteNote(note.remoteId)
                }
                noteDao.deleteNote(note)
            }
        }
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
