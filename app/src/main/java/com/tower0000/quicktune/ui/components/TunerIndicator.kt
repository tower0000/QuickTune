package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.tower0000.quicktune.R
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.Green
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.theme.Red
import com.tower0000.quicktune.ui.viewmodel.TunerState
import kotlin.math.cos
import kotlin.math.sin
import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center

private val INDICATOR_LENGTH = 12.dp
private val MAJOR_INDICATOR_LENGTH = 16.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp


@Composable
fun TunerIndicate(
    state: TunerState,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = LightGrey
    val pitchDiffFixed = if (state.pitchDiff > -30 && state.pitchDiff < 30) {
        state.pitchDiff
    } else if (state.pitchDiff <= -30) {
        -30f
    } else {
        30f
    }

    Canvas(modifier = modifier, onDraw = {

        val fullSize = Size(size.width, 1050f)

        drawRect(
            color = Color.Magenta,
            size = size
        )

        drawCircle(
            color = DarkGrey,
            center = Offset(center.x, center.y - size.height / 10),
            radius = 25f
        )

        drawArc(
            color = DarkGrey,
            startAngle = 210f,
            sweepAngle = 120f,
            useCenter = false,
            size = fullSize,
            style = Stroke(width = 5.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Red,
            startAngle = 270f,
            sweepAngle = pitchDiffFixed * 2,
            useCenter = false,
            style = Stroke(width = 5.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Green,
            startAngle = 262f,
            sweepAngle = 16f,
            useCenter = false,
            style = Stroke(width = 9.0.dp.toPx())
        )

        for (angle in 240 downTo 120 step 4) {
            val pitchDiffIndicator = 300 - angle

            val startOffset =
                pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_INITIAL_OFFSET.toPx(),
                    cX = center.x,
                    cY = center.y
                )

            if (pitchDiffIndicator % 20 == 0) {
                val markerOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                pitchMarker(startOffset, markerOffset, SolidColor(Color.White), 4.dp.toPx())
                pitchText(
                    pitch = (pitchDiffIndicator - 120) / 2,
                    angle = angle,
                    textMeasurer = textMeasurer,
                    textColor = textColor
                )

            } else {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                pitchMarker(startOffset, endOffset, SolidColor(Color.DarkGray), 1.dp.toPx())
            }
        }
        pitchIndicator(pitchAngle = 180 - pitchDiffFixed * 2)


        drawText(
            topLeft = Offset(x = size.width / 3 + size.width / 23, y = center.y - size.height / 3),
            textMeasurer = textMeasurer,
            text = "${String.format("%.0f", state.currentPitch)}Hz",
            style = TextStyle(
                color = textColor,
                fontSize = size.height.toSp() / 11,
                fontStyle = FontStyle.Italic
            )
        )
        drawText(
            topLeft = Offset(x = size.width / 3 + size.width / 12, center.y - size.height / 15),
            textMeasurer = textMeasurer,
            text = state.nearestNote,
            style = TextStyle(
                color = textColor,
                fontSize = size.height.toSp() / 7,
            )
        )
        drawText(
            topLeft = Offset(x = size.width / 3 + size.width / 10, center.y + size.height / 11),
            textMeasurer = textMeasurer,
            text = if (state.pitchDiff > 0) {
                "+${String.format("%.0f", state.pitchDiff)}"
            } else {
                String.format("%.0f", state.pitchDiff)
            },
            style = TextStyle(
                color = textColor,
                fontSize = size.height.toSp() / 18,
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
        style = TextStyle.Default.copy(lineHeight = TextUnit(0.0f, TextUnitType.Sp))
    )
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    val textOffset = pointOnCircle(
        thetaInDegrees = angle.toDouble(),
        radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx() - textWidth / 2 - INDICATOR_INITIAL_OFFSET.toPx(),
        cX = center.x,
        cY = center.y
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
        radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
        cX = center.x,
        cY = center.y
    )
    val fixedStart = Offset(center.x, center.y - size.height / 10)
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
