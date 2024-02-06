package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning

class Tunings {

    companion object {
        val STANDARD_TUNING = GuitarTuning(
            NoteService.getNote("E4")!!,
            NoteService.getNote("B3")!!,
            NoteService.getNote("G3")!!,
            NoteService.getNote("D3")!!,
            NoteService.getNote("A3")!!,
            NoteService.getNote("E2")!!
        )
    }
}