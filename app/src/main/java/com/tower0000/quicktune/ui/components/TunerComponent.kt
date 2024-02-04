package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.Pink
import com.tower0000.quicktune.ui.viewmodel.TunerState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TunerComponent(state: TunerState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {


        Box(
            modifier = Modifier
                .size(400.dp)
                .background(GreyBackground)
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                val screenWidth = 200.dp.toPx() * 5 / 2 // 5/2 - это коэффициент, чтобы взять вторую четверть экрана
                val screenHeight = 200.dp.toPx() * 2 // Удвоенная высота, чтобы взять вторую четверть экрана
                val arcSize = screenWidth / 2
                val start = Offset(screenWidth / 4f - arcSize / 2, screenHeight / 2f)
                val end = Offset(screenWidth / 4f + arcSize / 2, screenHeight / 2f)
                val radius = size.minDimension / 1.8f
                val indicatorAngle = state.currentPitchToAngle()

                val path = Path()
                path.moveTo(start.x, start.y)
                path.lineTo(end.x, end.y)

                val strokeWidth = 12.dp.toPx()

//                // Рисуем дугу
//                drawArc(
//                    color = Pink,
//                    startAngle = 215f,
//                    sweepAngle = 110f,
//                    useCenter = false,
//                    size = size,
//                    style = Stroke(width = strokeWidth)
//                )
//
//                // Рисуем палку
//                drawLine(
//                    color = Color.White,
//                    start = center,
//                    end = calculateIndicatorEndPoint(center, radius, indicatorAngle),
//                    strokeWidth = 4.dp.toPx()
//                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pitch: ${state.currentPitch}", color = Color.White)
        Text(text = "Nearest note: ${state.nearestNote}", color = Color.White)
        Text(text = "Pitch Diff: ${state.pitchDiff}", color = Color.White)
    }
}

fun calculateIndicatorEndPoint(center: Offset, radius: Float, angle: Float): Offset {
    val x = center.x + radius * cos(angle)
    val y = center.y + radius * sin(angle)
    return Offset(x, y)
}

fun Float.toRadians(): Float {
    return Math.toRadians(this.toDouble()).toFloat()
}

fun TunerState.currentPitchToAngle(): Float {
    val angleRange = 110f // Угол, на который рисуется индикатор (от -90 до 90 градусов)
    val normalizedPitch = (currentPitch - 1000f) / 10f // Нормализация питча (подстраивайте под ваши требования)

    // Преобразование нормализованного питча в угол
    val angle = (normalizedPitch * angleRange).coerceIn(-angleRange / 2f, angleRange / 2f)

    // Преобразование угла в радианы
    return angle.toRadians()
}