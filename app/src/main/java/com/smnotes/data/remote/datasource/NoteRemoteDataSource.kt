package com.smnotes.data.remote.datasource

import com.smnotes.data.remote.dto.NoteRequestDto
import com.smnotes.data.remote.dto.NoteResponseDto

interface NoteRemoteDataSource {
    suspend fun saveNote(dto: NoteRequestDto): NoteResponseDto
    suspend fun getAllNotes(): List<NoteResponseDto>
    suspend fun deleteNote(remoteId: String)
}
