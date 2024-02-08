package com.tower0000.quicktune.ui.viewmodel

import androidx.compose.ui.unit.dp
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
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject

private const val SAMPLE_RATE = 44100
private const val BUFFER_SIZE = 5376
class TunerViewModel : ViewModel() {

    private var isTuning = false
    private var currentPitch = 0.0f
    private var nearestNote = ""
    private var pitchDiff = 0.0f
    private var autoTuning = false
    private var tunedStrings = List(6) { false }
    private var selectedString: Int? = null
    private var selectedTuning = Tunings.STANDARD_TUNING

    private val pitchAnalyzer = PitchAnalyzer()

    private val stateSubject: BehaviorSubject<TunerState> = BehaviorSubject.create()


    fun processIntent(intent: TunerIntent) {
        when (intent) {
            is TunerIntent.StartTunerIntent -> startTuner()
            is TunerIntent.StopTunerIntent -> stopTuner()
            is TunerIntent.ChangeAutoTuning -> autoTuning = intent.isAutoTuning
            is TunerIntent.ChangeTuning -> TODO()
            is TunerIntent.PickString -> TODO()
        }
    }

    private fun updateState() {
        val newState = TunerState(
            isTuning =  isTuning,
            currentPitch = currentPitch,
            nearestNote = nearestNote,
            pitchDiff = pitchDiff,
            autoTuning = autoTuning,
            tunedStrings = tunedStrings,
            selectedString = selectedString,
            selectedTuning = selectedTuning)
        stateSubject.onNext(newState)
    }

    private val pitchHandler =
        PitchDetectionHandler { result, _ -> processPitch(result) }

    private fun processPitch(result: PitchDetectionResult) {
        if (result.pitch > 0)
            pitchAnalyzer.addPitch(result.pitch)
        if (pitchAnalyzer.checkLastFive()) {
            currentPitch = pitchAnalyzer.getGuitarPitch()
            TuningService.processPitch(currentPitch, Tunings.STANDARD_TUNING) { note, diff ->
                nearestNote = note.name
                pitchDiff = diff
            }
            updateState()
        } else {
            nearestNote = ""
            pitchDiff = 0.0f
            updateState()
        }
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
        autoTuning = true
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