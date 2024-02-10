package com.tower0000.quicktune.domain.service

import java.util.LinkedList
import kotlin.math.abs

class PitchAnalyzer {
    private val pitchQueue = LinkedList<Float>()

    fun addPitch(pitch: Float) {
        if (pitch > 40f && pitch < 400f)
            pitchQueue.offer(pitch)
        if (pitchQueue.size > 5) {
            pitchQueue.poll()
        }
    }

    fun checkLastFive(): Boolean {
        if (pitchQueue.size < 5) return false
        val tolerance = 5
        val lastPitch = pitchQueue.peekLast()
        for (pitch in pitchQueue) {
            if (abs(pitch - lastPitch!!) > tolerance)
                return false
        }
        return true
    }

    fun getGuitarPitch(): Float {
        return pitchQueue.peekLast()!!
    }

    fun analyzePitch(pitch: Float): Float {
        if (pitch > 40f && pitch < 400f)
            return pitch
        return 0f
    }
}