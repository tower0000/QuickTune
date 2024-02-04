package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.Note

class NoteService {
    companion object {
        private val noteList: List<Note> = listOf(
            Note("E2", 82.41f),
            Note("A2", 110.0f),
            Note("D3", 146.83f),
            Note("G3", 196.00f),
            Note("B3", 246.94f),
            Note("E4", 329.63f),
        )
        fun getNote(noteName: String): Note? {
            return noteList.find { it.name == noteName }
        }
    }
}