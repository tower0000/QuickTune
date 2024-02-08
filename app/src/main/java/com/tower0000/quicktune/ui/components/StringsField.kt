package com.tower0000.quicktune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.R
import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.entity.Note
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.LightGrey

@Composable
fun StringsField(
    font: FontFamily,
    tuning: GuitarTuning,
    tunedStrings: List<Boolean>,
    selectedString: Int?,
    onSelect: (Int) -> Unit
) {
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
            StringSelectionButton(0, tuning.firstString, tunedStrings[0], selectedString, onSelect, font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(1, tuning.secondString, tunedStrings[1], selectedString, onSelect, font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(2, tuning.thirdString, tunedStrings[2], selectedString, onSelect, font)
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
            StringSelectionButton(3, tuning.fourthString, tunedStrings[3], selectedString, onSelect, font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(4, tuning.fifthString, tunedStrings[4], selectedString, onSelect, font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringSelectionButton(5, tuning.sixthString, tunedStrings[5], selectedString, onSelect, font)
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
        if (tuned) Color.Green
        else if (selected == index) Color.Red
        else LightGrey,
        label = "String Button Content Color"
    )

    // Animate background color by selected state.
    val backgroundColor by animateColorAsState(
        if (selected == index) {
            contentColor.copy(alpha = 0.12f)
                .compositeOver(Color.Red)
        } else DarkGrey,
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