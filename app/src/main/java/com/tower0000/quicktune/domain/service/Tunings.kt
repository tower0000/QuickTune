package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.entity.Note

class Tunings {


//        val STANDARD_TUNING = GuitarTuning(
//            NoteService.getNote("E4")!!,
//            NoteService.getNote("B3")!!,
//            NoteService.getNote("G3")!!,
//            NoteService.getNote("D3")!!,
//            NoteService.getNote("A3")!!,
//            NoteService.getNote("E2")!!
//        )

    val STANDARD_TUNING: List<Note> = listOf(
        NoteService.getNote("E4")!!,
        NoteService.getNote("B3")!!,
        NoteService.getNote("G3")!!,
        NoteService.getNote("D3")!!,
        NoteService.getNote("A3")!!,
        NoteService.getNote("E2")!!
    )
}