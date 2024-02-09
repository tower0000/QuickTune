package com.tower0000.quicktune.ui.screens

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import com.tower0000.quicktune.domain.service.Tunings
import com.tower0000.quicktune.ui.components.StringsField
import com.tower0000.quicktune.ui.components.TuningsChangeBar
import com.tower0000.quicktune.ui.viewmodel.TunerIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(viewModel: TunerViewModel) {
    val tunings = Tunings()
    val defaultViewState = TunerState(
        true,
        0.0f,
        "--",
        0.0f,
        true,
        List(6) { false },
        null,
        tunings.STANDARD_TUNING
    )

    val state = remember {
        mutableStateOf(defaultViewState)
    }

    DisposableEffect(viewModel) {
        val disposable = viewModel.observeState()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newState ->
                state.value = newState
            }

        onDispose {
            disposable.dispose()
        }
    }

    val onSelectedString: (Int) -> Unit = { stringIndex ->
        viewModel.processIntent(TunerIntent.PickString(stringIndex))
        viewModel.processIntent(TunerIntent.ChangeAutoTuning(false))
    }

    val myFont = FontFamily(
        Font(resId = R.font.poppins_medium, weight = FontWeight.Normal)
    )
    val context = LocalContext.current
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
                    val switchCheckedState = state.value.autoTuning
                    Switch(
                        checked = state.value.autoTuning,
                        onCheckedChange = {
                            viewModel.processIntent(TunerIntent.ChangeAutoTuning(!switchCheckedState))
                            viewModel.processIntent(TunerIntent.PickString(null))},

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
                TunerIndicate(
                    state = state.value,
                    modifier = Modifier
                        .width(screenWidthInDp(context))
                        .height(screenWidthInDp(context) / 2)
                )
                Spacer(modifier = Modifier.padding(10.dp))
                StringsField(state = state.value,
                    fontFamily = myFont,
                    onSelect = onSelectedString)
            }
        }

    )

}

fun screenWidthInDp(context: Context): Dp {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For Android 11 (API level 30) and above
        context.display!!
    } else {
        // For versions below Android 11
        windowManager.defaultDisplay
    }
    val displayMetrics = DisplayMetrics()
    display.getMetrics(displayMetrics)
    val widthPixels = displayMetrics.widthPixels
    val density = displayMetrics.density
    return (widthPixels / density).dp
}


