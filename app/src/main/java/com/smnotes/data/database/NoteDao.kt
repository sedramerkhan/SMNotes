package com.smnotes.data.database

import androidx.room.*
import com.smnotes.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE syncStatus != 'PENDING_DELETE'")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE important = 1 AND syncStatus != 'PENDING_DELETE'")
    fun getImportantNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("UPDATE note SET syncStatus = :status, remoteId = :remoteId WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, remoteId: String?, status: SyncStatus)

    @Query("SELECT * FROM note WHERE syncStatus = 'PENDING_UPLOAD'")
    suspend fun getPendingUpload(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE syncStatus = 'PENDING_DELETE'")
    suspend fun getPendingDelete(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getNoteByRemoteId(remoteId: String): NoteEntity?

    @Query("UPDATE note SET syncStatus = 'LOCAL', remoteId = NULL WHERE syncStatus != 'LOCAL'")
    suspend fun resetAllSyncStatus()

    @Query("UPDATE note SET syncStatus = 'PENDING_UPLOAD' WHERE syncStatus = 'LOCAL'")
    suspend fun markLocalAsPendingUpload()

    @Query("UPDATE note SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markAsPendingDelete(id: Long)

    @Query("SELECT * FROM note WHERE remoteId IS NOT NULL")
    suspend fun getNotesWithRemoteId(): List<NoteEntity>

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()
}
