package com.tower0000.quicktune.ui.util

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
class PermissionHandler(
    private val activity: ComponentActivity,
    private val permission: String,
) {
    private var granted = false

    fun check(): Boolean {
        granted = checkPerm()
        return granted
    }

    private fun checkPerm(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}