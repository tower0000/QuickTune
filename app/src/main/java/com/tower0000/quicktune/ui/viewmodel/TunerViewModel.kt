package com.tower0000.quicktune.ui.viewmodel

import androidx.lifecycle.ViewModel
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import com.tower0000.quicktune.domain.service.TuningService
import com.tower0000.quicktune.domain.service.Tunings
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class TunerViewModel : ViewModel() {

    private var isTuning = false
    private var currentPitch = 0f
    private var nearestNote = "--"
    private var pitchDiff = 0f
    private val stateSubject: BehaviorSubject<TunerState> = BehaviorSubject.create()


    fun processIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartTunerIntent -> startTuner()
            is TunerIntent.StopTunerIntent -> stopTuner()
        }
    }

    private fun updateState() {
        val newState = TunerState(isTuning, currentPitch, nearestNote, pitchDiff)
        stateSubject.onNext(newState)
    }

    private val pitchHandler =
        PitchDetectionHandler { result, _ -> processPitch(result) }

    private fun processPitch(result: PitchDetectionResult) {
        currentPitch = result.pitch
        TuningService.processPitch(currentPitch, Tunings.STANDARD_TUNING) { note, diff ->
            nearestNote = note.name
            pitchDiff = diff
        }
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

    fun observeState(): Flowable<TunerState> {
        return stateSubject.toFlowable(BackpressureStrategy.DROP)
    }
}