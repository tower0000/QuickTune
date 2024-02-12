package com.tower0000.quicktune.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tower0000.quicktune.ui.screens.TunerScreen
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.QuickTuneTheme
import com.tower0000.quicktune.ui.viewmodel.TunerIntent
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel

class TunerActivity : ComponentActivity() {
    private val viewModel by viewModels<TunerViewModel>()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        viewModel.processIntent(TunerIntent.StartTuner)

        setContent {
            QuickTuneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GreyBackground
                ) {
                    val windowSizeClass = calculateWindowSizeClass(this)
                    TunerScreen(viewModel, windowSizeClass)
                }
            }
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
