package com.tower0000.quicktune.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tower0000.quicktune.ui.theme.QuickTuneTheme
import com.tower0000.quicktune.ui.viewmodel.TunerIntent
import com.tower0000.quicktune.ui.viewmodel.TunerState
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.flowOf

class TunerActivity : ComponentActivity() {
    private val viewModel by viewModels<TunerViewModel>()
    private lateinit var stateSubscription: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickTuneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        checkPermissions()
        viewModel.processIntent(TunerIntent.StartTunerIntent)

        stateSubscription = viewModel.observeState()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                processState(state)
            }
    }


    private fun checkPermissions() {
        val RECORD_AUDIO_PERMISSION = 123

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION
            )
        }
    }
}

private fun processState(state: TunerState){
    if (state.isTuning) {
        val pitch = state.currentPitch
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickTuneTheme {
        Greeting("Android")
    }
}