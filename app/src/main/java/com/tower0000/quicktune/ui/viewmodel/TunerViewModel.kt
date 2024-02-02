package com.tower0000.quicktune.ui.viewmodel

import androidx.lifecycle.ViewModel
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TunerViewModel: ViewModel()  {
    private val _state = MutableStateFlow(TunerState(false, 0f))
    val state: StateFlow<TunerState> = _state.asStateFlow()
    private var isTuning = false
    private var currentPitch = 0f

    fun processIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartTunerIntent -> startTuner()
            is TunerIntent.StopTunerIntent -> stopTuner()
        }
    }

    private fun updateState() {
        _state.value = TunerState(
            isTuning = isTuning,
            currentPitch = currentPitch
        )
    }

    private val pitchHandler =
        PitchDetectionHandler { result, _ -> processPitch(result) }

    private val sampleRate = 44100
    private val bufferSize = 5376
    private val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0)

    private val pitchProcessor = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, sampleRate.toFloat(), bufferSize, pitchHandler)

    init {
        dispatcher.addAudioProcessor(pitchProcessor)
    }


    private fun processPitch(result: PitchDetectionResult) {
        currentPitch = result.pitch
        updateState()
    }

    private fun startTuner() {
        Thread(dispatcher, "Audio Dispatcher").start()
        isTuning = true
        updateState()
    }

    private fun stopTuner() {
        dispatcher.stop()
        dispatcher.removeAudioProcessor(pitchProcessor)
        isTuning = false
        updateState()
    }
}