package com.tower0000.quicktune.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.tower0000.quicktune.domain.entity.GuitarTuning
import com.tower0000.quicktune.domain.service.Tunings
import com.tower0000.quicktune.ui.components.StringsField
import com.tower0000.quicktune.ui.components.TuningsChangeBar
import com.tower0000.quicktune.ui.viewmodel.TunerIntent
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tower0000.quicktune.ui.components.PitchDiffIndicator
import com.tower0000.quicktune.ui.theme.LightGrey
import com.tower0000.quicktune.ui.util.ScreenMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TunerScreen(viewModel: TunerViewModel, windowSizeClass: WindowSizeClass) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val fontFamily = FontFamily(Font(resId = R.font.poppins_medium, weight = FontWeight.Normal))
    val tunings = Tunings()
    val defaultViewState = TunerState(
        true, 0.0f, "--", 0.0f, true,
        List(6) { false }, null, tunings.STANDARD_TUNING
    )
    val state = remember { mutableStateOf(defaultViewState) }

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

    val onSelectedTuning: (GuitarTuning) -> Unit = { tuning ->
        viewModel.processIntent(TunerIntent.ChangeTuning(tuning))
    }

    val maxCompactScreenHeight = 600
    val screenMode: ScreenMode =
        if (screenHeight > maxCompactScreenHeight) ScreenMode.PORTRAIT
        else if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
        ) ScreenMode.LANDSCAPE
        else ScreenMode.COMPACT

    when (screenMode) {
        ScreenMode.PORTRAIT -> {
            Scaffold(
                containerColor = GreyBackground,
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .padding(2.dp),
                        title = {
                            Text(
                                stringResource(R.string.tv_topbar_name),
                                fontFamily = fontFamily
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = GreyBackground,
                            titleContentColor = Color.White
                        ),
                        actions = {
                            Text(
                                stringResource(R.string.tv_auto),
                                fontFamily = fontFamily,
                                color = Color.White
                            )
                            val switchCheckedState = state.value.autoTuning
                            Switch(
                                checked = state.value.autoTuning,
                                onCheckedChange = {
                                    viewModel.processIntent(TunerIntent.ChangeAutoTuning(!switchCheckedState))
                                    viewModel.processIntent(TunerIntent.PickString(null))
                                },

                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    )

                },
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TuningsChangeBar(
                            padding = it,
                            fontFamily = fontFamily,
                            tunings = tunings.ALL_TUNINGS,
                            selectedItem = state.value.selectedTuning,
                            onTuningSelected = onSelectedTuning
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        PitchDiffIndicator(
                            state = state.value,
                            fontFamily = fontFamily,
                            modifier = Modifier
                                .width(screenWidth.dp)
                        )
                        TunerIndicate(
                            state = state.value,
                            modifier = Modifier
                                .width(screenWidth.dp / 1.1f)
                                .height(screenWidth.dp / 2.2f)
                                .align(Alignment.CenterHorizontally)
                        )

                        Text(
                            modifier = Modifier
                                .padding(
                                    top = screenHeight.dp / 30,
                                    bottom = screenHeight.dp / 60,
                                )
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = state.value.nearestNote,
                            style = TextStyle(
                                color = LightGrey,
                                fontSize = 50.sp
                            )
                        )

                        StringsField(
                            state = state.value,
                            onSelect = onSelectedString,
                            modifier = Modifier
                                .height(screenHeight.dp / 2.5f)
                        )
                    }

                }

            )
        }

        ScreenMode.COMPACT -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val paddings = PaddingValues(top = 1.dp)
                Spacer(modifier = Modifier.padding(bottom = 15.dp))
                Row(
                    modifier = Modifier.height(55.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TuningsChangeBar(
                        padding = paddings,
                        fontFamily = fontFamily,
                        tunings = tunings.ALL_TUNINGS,
                        selectedItem = state.value.selectedTuning,
                        onTuningSelected = onSelectedTuning
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        stringResource(R.string.tv_auto),
                        fontFamily = fontFamily,
                        color = Color.White,
                    )
                    val switchCheckedState = state.value.autoTuning
                    Switch(
                        checked = state.value.autoTuning,
                        onCheckedChange = {
                            viewModel.processIntent(TunerIntent.ChangeAutoTuning(!switchCheckedState))
                            viewModel.processIntent(TunerIntent.PickString(null))
                        },

                        modifier = Modifier.padding(end = 15.dp)
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                PitchDiffIndicator(
                    state = state.value,
                    fontFamily = fontFamily,
                    modifier = Modifier
                        .width(screenWidth.dp)
                )
                TunerIndicate(
                    state = state.value,
                    modifier = Modifier
                        .width(screenWidth.dp / 1.2f)
                        .height(screenWidth.dp / 2.4f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = screenHeight.dp / 30,
                            bottom = screenHeight.dp / 60,
                        )
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = state.value.nearestNote,
                    style = TextStyle(
                        color = LightGrey,
                        fontSize = 50.sp
                    )
                )
                StringsField(
                    state = state.value,
                    onSelect = onSelectedString,
                    modifier = Modifier
                        .height(screenHeight.dp / 2.5f)
                )
            }
        }

        // else stays for Landscape screen mode
        else -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val paddings = PaddingValues(top = 1.dp)
                Spacer(modifier = Modifier.padding(bottom = 15.dp))
                Row(
                    modifier = Modifier.height(55.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TuningsChangeBar(
                        padding = paddings,
                        fontFamily = fontFamily,
                        tunings = tunings.ALL_TUNINGS,
                        selectedItem = state.value.selectedTuning,
                        onTuningSelected = onSelectedTuning
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        stringResource(R.string.tv_pitch_diff),
                        fontFamily = fontFamily,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    PitchDiffIndicator(
                        state = state.value,
                        fontFamily = fontFamily,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        stringResource(R.string.tv_auto),
                        fontFamily = fontFamily,
                        color = Color.White,
                    )
                    val switchCheckedState = state.value.autoTuning
                    Switch(
                        checked = state.value.autoTuning,
                        onCheckedChange = {
                            viewModel.processIntent(TunerIntent.ChangeAutoTuning(!switchCheckedState))
                            viewModel.processIntent(TunerIntent.PickString(null))
                        },

                        modifier = Modifier.padding(end = 15.dp)
                    )

                }

                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (indicator, text, strings, spacer) = createRefs()
                    Spacer(
                        modifier = Modifier
                            .constrainAs(spacer) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .height(screenHeight.dp / 9f))
                    TunerIndicate(
                        state = state.value,
                        modifier = Modifier
                            .constrainAs(indicator) {
                                top.linkTo(spacer.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(strings.start)
                            }
                            .width(screenWidth.dp / 2.5f)
                            .height(screenWidth.dp / 5f)
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(indicator.bottom)
                                start.linkTo(indicator.start)
                                end.linkTo(indicator.end)
                            }
                            .padding(
                                top = 20.dp,
                            ),
                        text = state.value.nearestNote,
                        style = TextStyle(
                            color = LightGrey,
                            fontSize = 50.sp
                        )
                    )

                    StringsField(
                        state = state.value,
                        onSelect = onSelectedString,
                        modifier = Modifier
                            .constrainAs(strings) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(indicator.end)
                                end.linkTo(parent.end)
                            }
                            .height(screenHeight.dp / 1.3f)
                            .width(screenWidth.dp / 2f)
                    )
                }


            }
        }
    }
}