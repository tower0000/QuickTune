package com.tower0000.quicktune.ui.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tower0000.quicktune.ui.screens.PermissionScreen
import com.tower0000.quicktune.ui.screens.TunerScreen
import com.tower0000.quicktune.ui.theme.GreyBackground
import com.tower0000.quicktune.ui.theme.QuickTuneTheme
import com.tower0000.quicktune.ui.util.PermissionHandler
import com.tower0000.quicktune.ui.viewmodel.TunerViewModel

class TunerActivity : ComponentActivity() {
    private val viewModel by viewModels<TunerViewModel>()
    private val permission = Manifest.permission.RECORD_AUDIO
    private val ph = PermissionHandler(this, permission)
    private val navTunerScreen = "tuner_screen"
    private val navPermissionScreen = "permission_screen"
    private lateinit var navController: NavHostController
    private val launcher =
        this.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) navController.navigate(navTunerScreen)
        }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            val windowSizeClass = calculateWindowSizeClass(this)
            QuickTuneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GreyBackground
                ) {
                    NavHost(
                        navController = navController, startDestination =
                        if (ph.check()) navTunerScreen
                        else navPermissionScreen
                    ) {
                        composable(navPermissionScreen) { PermissionScreen() }
                        composable(navTunerScreen) { TunerScreen(viewModel, windowSizeClass) }
                    }
                    if (!ph.check()) launcher.launch(permission)
                }
            }
        }
    }
}