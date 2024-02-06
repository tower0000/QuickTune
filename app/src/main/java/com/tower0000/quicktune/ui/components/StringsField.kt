package com.tower0000.quicktune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.entity.Note
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.LightGrey

@Composable
fun StringsField(font: FontFamily) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            StringTextField("1st", font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringTextField("2nd", font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringTextField("3rd", font)
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            OvalTextField("E4", font)
            Spacer(modifier = Modifier.padding(8.dp))
            OvalTextField("B3", font)
            Spacer(modifier = Modifier.padding(8.dp))
            OvalTextField("G3", font)
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start

        ) {
            OvalTextField("D3", font)
            Spacer(modifier = Modifier.padding(8.dp))
            OvalTextField("A3", font)
            Spacer(modifier = Modifier.padding(8.dp))
            OvalTextField("E2", font)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            StringTextField("4th", font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringTextField("5th", font)
            Spacer(modifier = Modifier.padding(8.dp))
            StringTextField("6th", font)
        }
    }
}

@Composable
fun OvalTextField(
    text: String,
    fontFamily: FontFamily,
) {
    Box(
        modifier = Modifier
            .size(70.dp, 50.dp)
            .background(
                color = DarkGrey,
                shape = MaterialTheme.shapes.small.copy(),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = 21.sp,
            color = Color.White
        )
    }
}


@Composable
private fun StringSelectionButton(
    index: Int,
    note: Note,
    tuned: Boolean,
    selected: Boolean,
    onSelect: (Int) -> Unit,
) {
    // Animate content color by selected and tuned state.
    val contentColor by animateColorAsState(
        if (tuned) Color.Green
        else if (selected) Color.Red
        else LightGrey,
        label = "String Button Content Color"
    )

    // Animate background color by selected state.
    val backgroundColor by animateColorAsState(
        if (selected) {
            contentColor.copy(alpha = 0.12f)
                .compositeOver(Color.Red)
        } else DarkGrey,
        label = "String Button Background Color"
    )

    // Selection Button
    OutlinedButton(
        modifier = Modifier.defaultMinSize(72.dp, 48.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(100),
        onClick = remember(onSelect, index) { { onSelect(index) } }
    ) {
        Text(note.name, modifier = Modifier.padding(4.dp))
    }
}





@Composable
fun StringTextField(
    text: String,
    fontFamily: FontFamily,
) {
    Box(
        modifier = Modifier
            .size(70.dp, 50.dp)
            .background(
                color = GreyBackground,
                shape = MaterialTheme.shapes.medium.copy(),
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}