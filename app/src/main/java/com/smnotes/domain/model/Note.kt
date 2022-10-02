package com.smnotes.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smnotes.presentation.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val important: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    companion object {
        val COLORS = listOf(White,RedOrange, LightGreen, Violet, BabyBlue, RedPink, Teal200, Purple200,Pink70,Cyan,Teal,Lime,
            Color.LightGray,Pink20)
    }
}

class InvalidNoteException(message: String): Exception(message)