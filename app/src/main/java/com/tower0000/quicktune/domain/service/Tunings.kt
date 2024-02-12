package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning

class Tunings {
    private val n = Notes()
    val STANDARD_TUNING: GuitarTuning = GuitarTuning("Standard", listOf(
        n.e4, n.b3, n.g3, n.d3, n.a2, n.e2))

    val DROP_D: GuitarTuning = GuitarTuning("Drop D", listOf(
        n.e4, n.b3, n.g3, n.d3, n.a2, n.d2))

    val DROP_C: GuitarTuning = GuitarTuning("Drop C", listOf(
        n.d4, n.a3, n.f3, n.c3, n.g2, n.c2))

    val ALL_TUNINGS: List<GuitarTuning> = listOf(STANDARD_TUNING, DROP_D, DROP_C)
}