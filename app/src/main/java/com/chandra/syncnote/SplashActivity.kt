package com.chandra.syncnote

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Show Android 12+ splash
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Keep splash on screen for 2 seconds
        splashScreen.setKeepOnScreenCondition { true } // temporarily keeps it
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 4000) // 4000ms = 4 seconds
    }
}


