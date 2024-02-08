package com.tower0000.quicktune.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.ui.theme.LightGrey

@Composable
fun TuningsChangeBar(padding: PaddingValues, font: FontFamily) {
    Column(
        modifier = Modifier.padding(padding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clickable { /* Handle tuning selection */ }
            ) {
                Spacer(modifier = Modifier.padding(7.dp))
                Text(
                    "Standart 6-string",
                    fontFamily = font,
                    fontSize = 11.sp,
                    color = LightGrey,
                )
                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    tint = LightGrey,
                    contentDescription = "Change tuning"
                )
            }
        }
    }
}
