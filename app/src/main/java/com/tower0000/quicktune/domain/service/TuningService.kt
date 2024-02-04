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
                allNotes.minByOrNull { abs(it.hzValue - currentPitch) }

            // If pitchDiff <0 -> note is bellow tune. if pitchDiff >0 -> note is higher than needed
            val pitchDiff = (currentPitch - nearestNote!!.hzValue)

            onResult(nearestNote, pitchDiff)
        }
    }
}