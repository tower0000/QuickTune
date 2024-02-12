package com.tower0000.quicktune.domain.service

import java.util.LinkedList
import kotlin.math.abs

class PitchAnalyzer {
    private val pitchQueue = LinkedList<Float>()

    fun analyzePitch(pitch: Float): Float {
        if (pitch > 40f && pitch < 400f)
            return pitch
        return 0f
    }
}