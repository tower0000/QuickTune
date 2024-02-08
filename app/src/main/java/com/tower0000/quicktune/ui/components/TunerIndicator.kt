package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.Green
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.theme.Red
import com.tower0000.quicktune.ui.viewmodel.TunerState
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.sp

private val INDICATOR_LENGTH = 12.dp
private val MAJOR_INDICATOR_LENGTH = 16.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp

@Composable
fun TunerIndicate(
    state: TunerState,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val pitchMaxValue = 30f
    val correctPitchLimit = 0.1
    val pitchDiffFontSize = 25.sp
    val pitchDiffText = String.format("%.0f", state.pitchDiff)
    val textColor = LightGrey

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = if (state.pitchDiff > 0) "+$pitchDiffText"
            else if (state.pitchDiff > -correctPitchLimit && state.pitchDiff < correctPitchLimit) ""
            else pitchDiffText,
            style = TextStyle(
                color = textColor,
                fontSize = pitchDiffFontSize,
            )
        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Canvas(modifier = modifier, onDraw = {
        val circleSizeForArc = Size(size.width, size.width)
        val pitchDiffFixed =
            if (state.pitchDiff > -pitchMaxValue && state.pitchDiff < pitchMaxValue)
                state.pitchDiff
            else if (state.pitchDiff <= -pitchMaxValue) -pitchMaxValue
            else pitchMaxValue
        drawCircle(
            color = DarkGrey,
            center = Offset(center.x, size.height),
            radius = 25f
        )

        drawArc(
            color = DarkGrey,
            startAngle = 206f,
            sweepAngle = 128f,
            useCenter = true,
            size = circleSizeForArc,
            style = Stroke(width = 4.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Red,
            startAngle = 270f,
            sweepAngle = pitchDiffFixed * 2,
            useCenter = false,
            size = circleSizeForArc,
            style = Stroke(width = 4.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Green,
            startAngle = 265f,
            sweepAngle = 10f,
            useCenter = false,
            size = circleSizeForArc,
            style = Stroke(width = 8.0.dp.toPx())
        )

        for (angle in 240 downTo 120 step 4) {
            val pitchDiffIndicator = 300 - angle
            val startOffset =
                pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height - INDICATOR_INITIAL_OFFSET.toPx(),
                    cX = center.x,
                    cY = size.height
                )

            if (pitchDiffIndicator % 20 == 0) {
                val markerOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height - MAJOR_INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = size.height
                )
                pitchMarker(startOffset, markerOffset, SolidColor(Color.White), 4.dp.toPx())
                pitchText(
                    pitch = (pitchDiffIndicator - 120) / 2,
                    angle = angle,
                    textMeasurer = textMeasurer,
                    textColor = Color.White
                )

            } else {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = size.height
                )
                pitchMarker(startOffset, endOffset, SolidColor(Color.DarkGray), 1.dp.toPx())
            }
        }
        pitchIndicator(pitchAngle = 180 - pitchDiffFixed * 2)

        val textSize = size.height.toSp() / 3

        drawText(
            topLeft = Offset(
                x = size.width / 2 - textSize.toPx() / 1.7f,
                y = center.y - textSize.toPx() / 1.5f
            ),
            textMeasurer = textMeasurer,
            text = state.nearestNote,
            style = TextStyle(
                color = Color.White,
                fontSize = textSize,
            )
        )
    })

}

private fun DrawScope.pitchMarker(
    startPoint: Offset,
    endPoint: Offset,
    brush: Brush,
    strokeWidth: Float
) {
    drawLine(brush = brush, start = startPoint, end = endPoint, strokeWidth = strokeWidth)
}

private fun DrawScope.pitchText(
    pitch: Int,
    angle: Int,
    textColor: Color,
    textMeasurer: TextMeasurer
) {
    val textLayoutResult = textMeasurer.measure(
        text = pitch.toString(),
        style = TextStyle(
            color = textColor,
            fontSize = 11.sp,
        )
    )
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    val textOffset = pointOnCircle(
        thetaInDegrees = angle.toDouble(),
        radius = size.height - MAJOR_INDICATOR_LENGTH.toPx() - textWidth / 1.2f,
        cX = center.x,
        cY = size.height
    )

    drawContext.canvas.save()
    drawContext.canvas.translate(
        textOffset.x - textWidth / 2,
        textOffset.y - textHeight / 2
    )

    drawText(textLayoutResult, color = textColor)

    drawContext.canvas.restore()
}

private fun DrawScope.pitchIndicator(
    pitchAngle: Float
) {
    val endOffset = pointOnCircle(
        thetaInDegrees = pitchAngle.toDouble(),
        radius = size.height - INDICATOR_LENGTH.toPx()/1.5f,
        cX = center.x,
        cY = size.height
    )
    val fixedStart = Offset(center.x, size.height)
    drawLine(
        color = Color.White,
        start = fixedStart,
        end = endOffset,
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round,
        alpha = 0.5f
    )
}

private fun pointOnCircle(
    thetaInDegrees: Double,
    radius: Float,
    cX: Float = 0f,
    cY: Float = 0f
): Offset {
    val x = cX + (radius * sin(Math.toRadians(thetaInDegrees)).toFloat())
    val y = cY + (radius * cos(Math.toRadians(thetaInDegrees)).toFloat())

    return Offset(x, y)
}
