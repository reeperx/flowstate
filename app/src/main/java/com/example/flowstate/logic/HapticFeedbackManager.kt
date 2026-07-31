package com.example.flowstate.logic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.flowstate.ui.theme.AppTheme

class HapticFeedbackManager(context: Context) {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun performHaptic(theme: AppTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = when (theme) {
                AppTheme.Studio -> VibrationEffect.createOneShot(10, 150)
                AppTheme.Club -> VibrationEffect.createOneShot(50, 255)
                AppTheme.Midnight -> VibrationEffect.createOneShot(5, 100)
                AppTheme.Vinyl -> VibrationEffect.createOneShot(20, 180)
                AppTheme.Prism -> VibrationEffect.createOneShot(15, 200)
                AppTheme.HighContrast -> VibrationEffect.createOneShot(30, 255)
            }
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
}
