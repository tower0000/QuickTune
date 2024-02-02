package com.tower0000.quicktune.ui.viewmodel

sealed class TunerIntent {
    data object StartTunerIntent : TunerIntent()
    data object StopTunerIntent : TunerIntent()
}