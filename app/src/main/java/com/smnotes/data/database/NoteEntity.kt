package com.smnotes.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smnotes.domain.model.Note
import com.smnotes.domain.model.SyncStatus

@Entity(tableName = "note")
data class NoteEntity(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Long,
    @ColumnInfo(name = "important") val isImportant: Boolean = false,
    val remoteId: String? = null,
    val syncStatus: SyncStatus = SyncStatus.LOCAL,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    fun toDomain(): Note = Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = color,
        isImportant = isImportant
    )
}

fun Note.toEntity(
    remoteId: String? = null,
    syncStatus: SyncStatus = SyncStatus.LOCAL
): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    timestamp = timestamp,
    color = color,
    isImportant = isImportant,
    remoteId = remoteId,
    syncStatus = syncStatus
)
