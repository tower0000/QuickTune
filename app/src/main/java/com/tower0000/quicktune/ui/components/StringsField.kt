package com.tower0000.quicktune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.R
import com.tower0000.quicktune.domain.entity.Note
import com.tower0000.quicktune.ui.screens.screenHeightInDp
import com.tower0000.quicktune.ui.screens.screenWidthInDp
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.Green
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.theme.SelectedStringGrey
import com.tower0000.quicktune.ui.viewmodel.TunerState

@Composable
fun StringsField(
    state: TunerState,
    onSelect: (Int) -> Unit,
    modifier: Modifier
) {
    val tuning = state.selectedTuning
    val tunedStrings = state.tunedStrings
    val selectedString = state.selectedString

    Row(modifier = modifier.fillMaxWidth()
        ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                for (index in 3..5) {
                    StringSelectionButton(
                        index,
                        tuning.tuning[index],
                        tunedStrings[index],
                        selectedString,
                        onSelect,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }

        }

        Column(
            modifier = Modifier
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.guitar_head_cropped),
                contentDescription = "Change tuning"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                for (index in 2 downTo 0) {
                    StringSelectionButton(
                        index,
                        tuning.tuning[index],
                        tunedStrings[index],
                        selectedString,
                        onSelect,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
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
) {
    val contentColor by animateColorAsState(
        Color.White,
        label = "String Button Content Color"
    )

    val backgroundColor by animateColorAsState(
        if (selected == index) {
            contentColor.copy(alpha = 0.12f)
                .compositeOver(SelectedStringGrey)
        } else if (tuned) Green
        else DarkGrey,
        label = "String Button Background Color"
    )

    OutlinedButton(
        modifier = Modifier.height(45.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        onClick = remember(onSelect, index) { { onSelect(index) } }
    ) {
        Text(
            note.name,
            color = Color.White,
            fontSize = 17.sp,
            maxLines = 1
        )
    }
}