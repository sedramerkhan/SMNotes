package com.smnotes.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponseDto(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: String,
    @SerialName("important") val isImportant: Boolean
)
