package com.smnotes.data.remote.datasource

import com.smnotes.data.remote.dto.NoteRequestDto
import com.smnotes.data.remote.dto.NoteResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class NoteRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : NoteRemoteDataSource {

    override suspend fun saveNote(dto: NoteRequestDto): NoteResponseDto {
        return client.post("$baseUrl/notes") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }.body()
    }

    override suspend fun getAllNotes(): List<NoteResponseDto> {
        return client.get("$baseUrl/notes").body()
    }

    override suspend fun deleteNote(remoteId: String) {
        client.delete("$baseUrl/notes/$remoteId")
    }
}
