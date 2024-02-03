package com.tower0000.quicktune.ui.viewmodel

import androidx.lifecycle.ViewModel
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TunerViewModel : ViewModel() {

    private var isTuning = false
    private var currentPitch = 0f
    private val stateSubject = BehaviorSubject.createDefault(TunerState(isTuning, currentPitch))


    fun processIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartTunerIntent -> startTuner()
            is TunerIntent.StopTunerIntent -> stopTuner()
        }
    }

    private fun updateState() {
        val newState = TunerState(isTuning, currentPitch)
        stateSubject.onNext(newState)
    }

    private val pitchHandler =
        PitchDetectionHandler { result, _ -> processPitch(result) }

    private fun processPitch(result: PitchDetectionResult) {
        currentPitch = result.pitch
        updateState()
    }

    private val sampleRate = 44100
    private val bufferSize = 5376
    private val dispatcher: AudioDispatcher =
        AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0)

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.YIN,
        sampleRate.toFloat(),
        bufferSize,
        pitchHandler
    )

    init {
        dispatcher.addAudioProcessor(pitchProcessor)
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

    fun observeState(): Observable<TunerState> {
        return stateSubject.hide()
    }
}