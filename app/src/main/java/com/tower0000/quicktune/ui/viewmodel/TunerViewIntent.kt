package com.tower0000.quicktune.ui.viewmodel

import com.tower0000.quicktune.domain.entity.GuitarTuning

sealed class TunerIntent {
    data object StartTuner : TunerIntent()
    data object StopTuner : TunerIntent()
    data class ChangeAutoTuning(val isAutoTuning: Boolean) : TunerIntent()
    data class ChangeTuning(val tuning: GuitarTuning) : TunerIntent()
    data class PickString(val guitarString: Int?) : TunerIntent()
}