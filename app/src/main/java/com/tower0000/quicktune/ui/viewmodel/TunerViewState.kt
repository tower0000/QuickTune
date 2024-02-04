package com.tower0000.quicktune.ui.viewmodel

import com.tower0000.quicktune.domain.entity.Note

data class TunerState(
    val isTuning: Boolean,
    val currentPitch: Float,
    val nearestNote: String,
    val pitchDiff: Float
)