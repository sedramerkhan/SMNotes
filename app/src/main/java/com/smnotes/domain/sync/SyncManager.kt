package com.smnotes.domain.sync

interface SyncManager {
    suspend fun syncOnLogin()
    suspend fun syncPending()
    suspend fun syncNote(localId: Long)
    suspend fun syncDeleteNote(localId: Long)
}
