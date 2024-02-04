package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.entity.Note
import kotlin.math.abs

class TuningService {

    companion object {
        fun processPitch(currentPitch: Float, guitarTuning: GuitarTuning, onResult: (Note, Float) -> Unit) {
            val allNotes = listOf(
                guitarTuning.firstString,
                guitarTuning.secondString,
                guitarTuning.thirdString,
                guitarTuning.fourthString,
                guitarTuning.fifthString,
                guitarTuning.sixthString
            )

            val nearestNote =
                allNotes.minByOrNull { abs((it.botHzLimit + it.upHzLimit) / 2 - currentPitch) }

            // If pitchDiff >0 -> note is bellow tune. if pitchDiff <0 -> note is higher than needed
            val pitchDiff = (nearestNote!!.botHzLimit + nearestNote.upHzLimit) / 2 - currentPitch

            onResult(nearestNote, pitchDiff)
        }
    }
}