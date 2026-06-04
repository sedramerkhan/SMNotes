package com.smnotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
@Database(
    entities = [NoteEntity::class],
    version = 4,
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

        // Removes the redundant pendingDelete column; sync state is now tracked solely via syncStatus.
        // Uses table-rebuild for compatibility with SQLite < 3.35 (pre-API 32 devices).
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE note_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        content TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        color INTEGER NOT NULL,
                        important INTEGER NOT NULL DEFAULT 0,
                        remoteId TEXT DEFAULT NULL,
                        syncStatus TEXT NOT NULL DEFAULT 'LOCAL'
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO note_new (id, title, content, timestamp, color, important, remoteId, syncStatus)
                    SELECT id, title, content, timestamp, color, important, remoteId, syncStatus FROM note
                """.trimIndent())
                db.execSQL("DROP TABLE note")
                db.execSQL("ALTER TABLE note_new RENAME TO note")
            }
        }
    }
}
