package com.tower0000.quicktune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.ui.theme.GreyBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuningsChangeBar(
    padding: PaddingValues,
    tunings: List<GuitarTuning>,
    onTuningSelected: (GuitarTuning) -> Unit,
    selectedItem: GuitarTuning,
    fontFamily: FontFamily
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedItem.name) }

    Box(
        modifier = Modifier
            .padding(start = 15.dp, top = padding.calculateTopPadding()),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            val containerColor by animateColorAsState(
                GreyBackground,
                label = "String Button Content Color"
            )

            val textStyle = TextStyle(
                color = Color.White
            )

            val keyboardController = LocalSoftwareKeyboardController.current

            OutlinedTextField(
                value = "Tuning:  $selectedText",
                modifier = Modifier
                    .menuAnchor()
                    .height(50.dp)
                    .width(210.dp)
                    .onFocusChanged { hasFocus ->
                        if (hasFocus.hasFocus) {
                            keyboardController?.hide()
                        }
                    },
                textStyle = textStyle,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkGray,
                    unfocusedContainerColor = DarkGray,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    unfocusedIndicatorColor = containerColor,
                    focusedIndicatorColor = containerColor
                ),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                tunings.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.name,
                                fontFamily = fontFamily,
                                fontSize = 12.sp
                            )
                        },
                        onClick = {
                            onTuningSelected(item)
                            selectedText = item.name
                            expanded = false
                        },
                        modifier = Modifier.height(30.dp),
                        trailingIcon = {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}