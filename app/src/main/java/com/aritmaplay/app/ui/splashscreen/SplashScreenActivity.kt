package com.aritmaplay.app.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aritmaplay.app.MainActivity
import com.aritmaplay.app.R
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.ui.onboarding.OnBoardingActivity
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSystemBar()

        val userPreference = UserPreference.getInstance(dataStore)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setupSystemBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
        }
    }
}