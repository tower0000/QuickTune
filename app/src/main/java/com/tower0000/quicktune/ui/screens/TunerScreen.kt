package com.tower0000.quicktune.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tower0000.quicktune.ui.components.TunerComponent
import com.tower0000.quicktune.ui.viewmodel.TunerState
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@Composable
fun TunerScreen(viewModel: TunerViewModel) {

    val state = remember { mutableStateOf(TunerState(false, 0f)) }

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

    TunerComponent(state.value)
}