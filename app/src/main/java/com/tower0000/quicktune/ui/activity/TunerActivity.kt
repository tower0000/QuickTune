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
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel
import kotlinx.coroutines.flow.flowOf

class TunerActivity : ComponentActivity() {
    private val viewModel by viewModels<TunerViewModel>()
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

//        val flow = flowOf(viewModel.state)
//
//        val flowable = flow
//            .asFlow() // Convert Flow to RxJava Flowable
//            .rxFlowable() // Use rxFlowable extension function
//
//        compositeDisposable.add(
//            flowable.subscribeBy(
//                onNext = { value ->
//                    // Handle onNext
//                },
//                onError = { error ->
//                    // Handle onError
//                },
//                onComplete = {
//                    // Handle onComplete
//                }
//            )
//        )
//
//
//        viewModel.state.collect { state ->
//            updateUI(state)
//        }
//
//
//        val RECORD_AUDIO_PERMISSION = 123
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.RECORD_AUDIO),
//                RECORD_AUDIO_PERMISSION
//            )
//            val tuner = Tuner()
//            tuner.startTuner()
//
//        } else {
//            val tuner = Tuner()
//            tuner.startTuner()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        compositeDisposable.clear()
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