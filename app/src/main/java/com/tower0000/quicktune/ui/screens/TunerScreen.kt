package com.tower0000.quicktune.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tower0000.quicktune.ui.components.TunerIndicate
import com.tower0000.quicktune.ui.viewmodel.TunerState
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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

    TunerIndicate(
        state = state.value,
        modifier = Modifier
            .padding(90.dp)
            .requiredSize(350.dp)
    )
}