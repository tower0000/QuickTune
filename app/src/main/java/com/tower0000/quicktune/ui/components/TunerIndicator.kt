package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.Green
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.theme.Red
import com.tower0000.quicktune.ui.viewmodel.TunerState
import kotlin.math.cos
import kotlin.math.sin

private val INDICATOR_LENGTH = 14.dp
private val MAJOR_INDICATOR_LENGTH = 18.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp

@Composable
fun TunerIndicate(
    state: TunerState,
    modifier: Modifier = Modifier
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
        drawCircle(
            color = DarkGrey,
            center = center,
            radius = 25f
        )

        drawArc(
            color = DarkGrey,
            startAngle = 30f,
            sweepAngle = -240f,
            useCenter = false,
            style = Stroke(width = 4.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Red,
            startAngle = 270f,
            sweepAngle = pitchDiffFixed * 4,
            useCenter = false,
            style = Stroke(width = 4.0.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Green,
            startAngle = 262f,
            sweepAngle = 16f,
            useCenter = false,
            style = Stroke(width = 4.0.dp.toPx())
        )

        for (angle in 300 downTo 60 step 4) {
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
                    pitch = (pitchDiffIndicator - 120)/4,
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

        pitchIndicator(pitchAngle = 180 - pitchDiffFixed * 4)
    })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.requiredSize(95.dp))
        Text(
            text = "${String.format("%.0f", state.currentPitch)}Hz",
            color = textColor,
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.requiredSize(145.dp))

        Text(
            text = state.nearestNote,
            color = textColor,
            fontSize = 55.sp
        )

        Text(
            text = if (state.pitchDiff > 0) {
                "+${String.format("%.0f", state.pitchDiff)}"
            } else {
                String.format("%.0f", state.pitchDiff)
            },
            color = textColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,

            )


    }
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

    drawLine(
        color = Color.White,
        start = center,
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
