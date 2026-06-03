package com.smnotes.data.database

import androidx.room.*
import com.smnotes.domain.model.Note
import com.smnotes.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE pendingDelete = 0")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE important = 1 AND pendingDelete = 0")
    fun getImportantNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE note SET syncStatus = :status, remoteId = :remoteId WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, remoteId: String?, status: SyncStatus)

    @Query("SELECT * FROM note WHERE syncStatus = 'PENDING_UPLOAD' AND pendingDelete = 0")
    suspend fun getPendingUpload(): List<Note>

    @Query("SELECT * FROM note WHERE pendingDelete = 1")
    suspend fun getPendingDelete(): List<Note>

    @Query("SELECT * FROM note WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getNoteByRemoteId(remoteId: String): Note?

    @Query("UPDATE note SET syncStatus = 'LOCAL', remoteId = NULL WHERE syncStatus != 'LOCAL'")
    suspend fun resetAllSyncStatus()

    @Query("UPDATE note SET syncStatus = 'PENDING_UPLOAD' WHERE syncStatus = 'LOCAL'")
    suspend fun markLocalAsPendingUpload()

    @Query("UPDATE note SET pendingDelete = 1, syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markAsPendingDelete(id: Long)

    @Query("SELECT * FROM note WHERE remoteId IS NOT NULL")
    suspend fun getNotesWithRemoteId(): List<Note>

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()
}
