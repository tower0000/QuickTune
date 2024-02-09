package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.entity.Note
import kotlin.math.abs

class TuningService {

    companion object {
        fun processPitch(currentPitch: Float, guitarTuning: List<Note>, onResult: (Note, Float) -> Unit) {

            val nearestNote =
                guitarTuning.minByOrNull { abs(it.hzValue - currentPitch) }

            // If pitchDiff <0 -> note is bellow tune. if pitchDiff >0 -> note is higher than needed
            val pitchDiff = (currentPitch - nearestNote!!.hzValue)
            //val nearestNoteIndex = guitarTuning.indexOf(nearestNote)
            onResult(nearestNote, pitchDiff)
        }
    }
}