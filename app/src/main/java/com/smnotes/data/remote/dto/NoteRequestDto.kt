package com.smnotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequestDto(
    val id: String? = null,
    val title: String,
    val content: String,
    val color: Long,
    val isImportant: Boolean = false
)
