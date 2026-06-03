package com.smnotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smnotes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "SMNotes"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE note ADD COLUMN remoteId TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE note ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'LOCAL'")
                db.execSQL("ALTER TABLE note ADD COLUMN pendingDelete INTEGER NOT NULL DEFAULT 0")
                // color Int→Long and important→isImportant: SQLite storage unchanged, no SQL needed
            }
        }
    }
}
