package com.tower0000.quicktune.domain.service

import com.tower0000.quicktune.domain.entity.GuitarTuning

class Tunings {
    private val n = Notes()
    val STANDARD_TUNING = GuitarTuning(
        "Standard", listOf(n.e4, n.b3, n.g3, n.d3, n.a2, n.e2)
    )

    val DROP_D = GuitarTuning(
        "Drop D", listOf(n.e4, n.b3, n.g3, n.d3, n.a2, n.d2)
    )

    val DOUBLE_DROP_D = GuitarTuning(
        "Double Drop D", listOf(n.d4, n.b3, n.g3, n.d3, n.a2, n.d2)
    )

    val DROP_C = GuitarTuning(
        "Drop C", listOf(n.d4, n.a3, n.f3, n.c3, n.g2, n.c2)
    )

    val DADGAD = GuitarTuning(
        "DADGAD", listOf(n.d4, n.a3, n.g3, n.d3, n.a2, n.d2)
    )

    val OPEN_D = GuitarTuning(
        "Open D", listOf(n.d4, n.a3, n.fis3, n.d3, n.a2, n.d2)
    )

    val OPEN_D_MINOR = GuitarTuning(
        "Open D Minor", listOf(n.d4, n.a3, n.f3, n.d3, n.a2, n.d2)
    )

    val OPEN_G = GuitarTuning(
        "Open G", listOf(n.d4, n.b3, n.g3, n.d3, n.g2, n.d2)
    )

    val ALL_TUNINGS: List<GuitarTuning> = listOf(
        STANDARD_TUNING, DROP_D, DOUBLE_DROP_D, DROP_C, DADGAD, OPEN_D, OPEN_D_MINOR, OPEN_G
    )
}