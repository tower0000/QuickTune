package com.tower0000.quicktune.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.tower0000.quicktune.R
import com.tower0000.quicktune.ui.components.TunerIndicate
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.viewmodel.TunerState
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import androidx.compose.ui.platform.LocalContext
import com.tower0000.quicktune.ui.components.StringsField
import com.tower0000.quicktune.ui.components.TuningsChangeBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(viewModel: TunerViewModel) {
    val state = remember { mutableStateOf(TunerState(false, 0.0f, "", 0.0f)) }

    DisposableEffect(viewModel) {
        val disposable = viewModel.observeState()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
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
            ) {
                TuningsChangeBar(padding = it, font = myFont)
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TunerIndicate(
                        state = state.value,
//                        modifier = Modifier
//                            .requiredSize(390.dp)
                    )
                }
            }
        }

    )

}


