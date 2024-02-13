package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.viewmodel.TunerState

@Composable
fun PitchDiffIndicator(state: TunerState, fontFamily: FontFamily, modifier: Modifier) {
    val correctPitchLimit = 0.1
    val pitchDiffFontSize = 24.sp
    val pitchDiffText = String.format("%.1f", state.pitchDiff)
    val textColor = LightGrey

    Spacer(modifier = Modifier.padding(5.dp))
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = if (state.pitchDiff > 0) "+$pitchDiffText"
            else if (state.pitchDiff > -correctPitchLimit && state.pitchDiff < correctPitchLimit) ""
            else pitchDiffText,
            style = TextStyle(
                color = textColor,
                fontSize = pitchDiffFontSize,
                fontFamily = fontFamily
            ),
        )
    }
    Spacer(modifier = Modifier.padding(6.dp))
}