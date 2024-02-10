package com.tower0000.quicktune.ui.viewmodel
import com.tower0000.quicktune.domain.entity.GuitarTuning

data class TunerState(
    val isTuning: Boolean,
    val currentPitch: Float,
    val nearestNote: String,
    val pitchDiff: Float,
    val autoTuning: Boolean,
    val tunedStrings: List<Boolean>,
    val selectedString: Int?,
    val selectedTuning: GuitarTuning
)