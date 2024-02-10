package com.tower0000.quicktune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.R
import com.tower0000.quicktune.domain.entity.Note
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.Green
import com.tower0000.quicktune.ui.theme.SelectedStringGrey
import com.tower0000.quicktune.ui.viewmodel.TunerState

@Composable
fun StringsField(
    state: TunerState,
    fontFamily: FontFamily,
    onSelect: (Int) -> Unit
) {
    val tuning = state.selectedTuning
    val tunedStrings = state.tunedStrings
    val selectedString = state.selectedString
    Row(
        modifier = Modifier.padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))

            StringSelectionButton(2, tuning.tuning[2], tunedStrings[2], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(1, tuning.tuning[1], tunedStrings[1], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(0, tuning.tuning[0], tunedStrings[0], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.guitar_head_cropped),
                contentDescription = "Change tuning"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            StringSelectionButton(3, tuning.tuning[3], tunedStrings[3], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(4, tuning.tuning[4], tunedStrings[4], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(5, tuning.tuning[5], tunedStrings[5], selectedString, onSelect, fontFamily)
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}


@Composable
private fun StringSelectionButton(
    index: Int,
    note: Note,
    tuned: Boolean,
    selected: Int?,
    onSelect: (Int) -> Unit,
    fontFamily: FontFamily
) {
    // Animate content color by selected and tuned state.
    val contentColor by animateColorAsState(
        Color.White,
        label = "String Button Content Color"
    )

    // Animate background color by selected state.
    val backgroundColor by animateColorAsState(
        if (selected == index) {
            contentColor.copy(alpha = 0.12f)
                .compositeOver(SelectedStringGrey)
        } else if (tuned) Green
        else DarkGrey,
        label = "String Button Background Color"
    )

    // Selection Button
    OutlinedButton(
        modifier = Modifier.defaultMinSize(56.dp, 48.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(100),
        onClick = remember(onSelect, index) { { onSelect(index) } }
    ) {
        Text(note.name,fontFamily = fontFamily,
            fontSize = 12.sp,
            color = Color.White)
    }
}