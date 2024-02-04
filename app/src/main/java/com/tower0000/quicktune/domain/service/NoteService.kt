package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.Note

class NoteService {
    companion object {
        private val noteList: List<Note> = listOf(
            Note("E2", 82.41f,87.31f),
            Note("A2", 110.0f,116.54f),
            Note("D3", 146.83f,155.56f),
            Note("G3", 196.00f,207.65f),
            Note("B3", 246.94f,261.63f),
            Note("E4", 329.63f,349.23f),
        )
        fun getNote(noteName: String): Note? {
            return noteList.find { it.name == noteName }
        }
    }
}