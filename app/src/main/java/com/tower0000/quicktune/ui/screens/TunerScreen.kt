package com.tower0000.quicktune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tower0000.quicktune.R
import com.tower0000.quicktune.ui.components.TunerIndicate
import com.tower0000.quicktune.ui.theme.DarkGrey
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.theme.Pink40
import com.tower0000.quicktune.ui.viewmodel.TunerState
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(viewModel: TunerViewModel) {

    val state = remember { mutableStateOf(TunerState(false, 0.0f, "", 0.0f)) }

    DisposableEffect(viewModel) {
        val disposable = viewModel.observeState()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(200, TimeUnit.MILLISECONDS)
            .subscribe { newState ->
                if (newState.currentPitch < 1f) {
                    state.value = TunerState(true, 0.0f, "", 0.0f)
                } else {
                    state.value = newState
                }
            }

        onDispose {
            disposable.dispose()
        }
    }


    var isAutoMode by remember { mutableStateOf(true) }
    val myFont = FontFamily(
        Font(resId = R.font.poppins_medium, weight = FontWeight.Normal)
    )

    Scaffold(

        containerColor = GreyBackground,
        topBar = {
            TopAppBar(

                modifier = Modifier
                    .padding(2.dp),
                title = { Text("QuickTune", fontFamily = myFont) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreyBackground,
                    titleContentColor = Color.White
                ),
                actions = {
                    Text("Auto  ", fontFamily = myFont, color = Color.White)
                    Switch(
                        checked = isAutoMode,
                        onCheckedChange = { isAutoMode = it },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            )
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .padding(it)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                                fontFamily = myFont,
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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TunerIndicate(
                        state = state.value,
                        modifier = Modifier
                            .requiredSize(330.dp)
                    )

                }

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        StringTextField("1st", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        StringTextField("2nd", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        StringTextField("3rd", myFont)
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        OvalTextField("E4", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        OvalTextField("B3", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        OvalTextField("G3", myFont)
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.Start

                    ) {
                        OvalTextField("D3", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        OvalTextField("A3", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        OvalTextField("E2", myFont)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        StringTextField("4th", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        StringTextField("5th", myFont)
                        Spacer(modifier = Modifier.padding(8.dp))
                        StringTextField("6th", myFont)
                    }
                }

            }
        }
    )



//    Row(
//        modifier = Modifier
//            .padding(45.dp),
//        verticalAlignment = Alignment.Bottom,
//        horizontalArrangement = Arrangement.SpaceBetween
//
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            horizontalAlignment = Alignment.End
//        ) {
//            StringTextField("1st", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            StringTextField("2nd", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            StringTextField("3rd", myFont)
//        }
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            horizontalAlignment = Alignment.End
//        ) {
//            OvalTextField("E4", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            OvalTextField("B3", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            OvalTextField("G3", myFont)
//        }
//
//        Spacer(modifier = Modifier.padding(8.dp))
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            horizontalAlignment = Alignment.Start
//
//        ) {
//            OvalTextField("D3", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            OvalTextField("A3", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            OvalTextField("E2", myFont)
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            horizontalAlignment = Alignment.Start
//        ) {
//            StringTextField("4th", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            StringTextField("5th", myFont)
//            Spacer(modifier = Modifier.padding(8.dp))
//            StringTextField("6th", myFont)
//        }
//    }

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
        contentAlignment = Alignment.Center
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