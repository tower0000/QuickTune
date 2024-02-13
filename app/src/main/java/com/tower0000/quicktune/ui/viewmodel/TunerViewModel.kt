package com.tower0000.quicktune.ui.viewmodel

import android.Manifest
import androidx.lifecycle.ViewModel
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.service.PitchAnalyzer
import com.tower0000.quicktune.domain.service.TuningService
import com.tower0000.quicktune.domain.service.Tunings
import com.tower0000.quicktune.ui.util.PermissionHandler
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject

private const val SAMPLE_RATE = 44100
private const val BUFFER_SIZE = 5376

class TunerViewModel : ViewModel() {
    private val tunings = Tunings()
    private var isTuning = false
    private var currentPitch = 0.0f
    private var nearestNote = "--"
    private var pitchDiff = 0.0f
    private var autoTuning = true
    private var tunedStrings = MutableList(6) { false }
    private var selectedString: Int? = null
    private var selectedTuning = tunings.STANDARD_TUNING

    private val pitchAnalyzer = PitchAnalyzer()

    private val stateSubject: BehaviorSubject<TunerState> = BehaviorSubject.create()


    fun processIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartTuner -> startTuner()
            is TunerIntent.StopTuner -> stopTuner()
            is TunerIntent.ChangeAutoTuning -> autoTuning = intent.isAutoTuning
            is TunerIntent.ChangeTuning -> changeGuitarTuning(intent.tuning)
            is TunerIntent.PickString -> pickGuitarString(intent.guitarString)
        }
    }

    private fun updateState() {
        val newState = TunerState(
            isTuning = isTuning,
            currentPitch = currentPitch,
            nearestNote = nearestNote,
            pitchDiff = pitchDiff,
            autoTuning = autoTuning,
            tunedStrings = tunedStrings,
            selectedString = selectedString,
            selectedTuning = selectedTuning
        )
        stateSubject.onNext(newState)
    }

    private val pitchHandler =
        PitchDetectionHandler { result, _ -> processPitch(result) }

    private fun processPitch(result: PitchDetectionResult) {
        if (!autoTuning && selectedString != null) detectPitchCurrentString(result)
        else if (autoTuning) detectPitchAuto(result)
    }

    private val dispatcher: AudioDispatcher =
        AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, 0)

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.YIN,
        SAMPLE_RATE.toFloat(),
        BUFFER_SIZE,
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

    private fun pickGuitarString(index: Int?) {
        selectedString = index
        updateState()
    }

    private fun changeGuitarTuning(tuning: GuitarTuning) {
        selectedTuning = tuning
        tunedStrings.forEachIndexed { index, _ ->
            tunedStrings[index] = false
        }
        updateState()
    }

    private fun detectPitchAuto(result: PitchDetectionResult) {
        if (pitchAnalyzer.analyzePitch(result.pitch) > 0) {
            currentPitch = result.pitch
            TuningService.processPitch(currentPitch, selectedTuning.tuning) { note, diff ->
                nearestNote = note.name
                pitchDiff = diff
                if (pitchDiff > -1f && pitchDiff < 1f) {
                    val index = selectedTuning.tuning.indexOf(note)
                    tunedStrings[index] = true
                }
            }
        } else {
            nearestNote = "--"
            pitchDiff = 0.0f
        }
        updateState()
    }

    private fun detectPitchCurrentString(result: PitchDetectionResult) {
        val index = selectedString!!
        if (pitchAnalyzer.analyzePitch(result.pitch) > 0) {
            TuningService.processPitchFromCurrentString(
                result.pitch,
                selectedTuning.tuning[selectedString!!]
            ) { diff ->
                pitchDiff = diff
                if (pitchDiff > -1f && pitchDiff < 1f) {
                    tunedStrings[index] = true
                }
            }
        } else pitchDiff = 0.0f
        nearestNote = selectedTuning.tuning[index].name
        updateState()
    }

    fun observeState(): Flowable<TunerState> {
        return stateSubject.toFlowable(BackpressureStrategy.DROP)
    }
}