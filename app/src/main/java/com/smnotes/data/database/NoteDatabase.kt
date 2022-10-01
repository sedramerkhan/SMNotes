package com.smnotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smnotes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "SMNotes"
    }
}