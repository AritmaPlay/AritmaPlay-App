package com.aritmaplay.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.aritmaplay.app.R
import com.aritmaplay.app.databinding.ActivityOnBoardingBinding
import com.aritmaplay.app.ui.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private var currentStep = 0
    private lateinit var onboardingData: List<Pair<Int, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBar()

        onboardingData = listOf(
            Pair(R.drawable.on_boarding_1, getString(R.string.onboarding_text1)),
            Pair(R.drawable.on_boarding_2, getString(R.string.onboarding_text2)),
            Pair(R.drawable.on_boarding_3, getString(R.string.onboarding_text3))
        )

        updateOnboardingScreen()

        binding.skipButton.setOnClickListener {
            goToLoginActivity()
        }

        binding.nextButton.setOnClickListener {
            if (currentStep < onboardingData.size - 1) {
                currentStep++
                updateOnboardingScreen()
            } else {
                goToLoginActivity()
            }
        }
    }

    private fun updateOnboardingScreen() {
        val (imageRes, text) = onboardingData[currentStep]
        binding.onBoardingImage.setImageResource(imageRes)
        binding.onBoardingText.text = text

        val progressPercentage = ((currentStep + 1) * 100) / onboardingData.size
        binding.progressbar.progress = progressPercentage

        binding.nextButton.text = if (currentStep == onboardingData.size - 1) getString(R.string.start) else getString(R.string.next)
        binding.skipButton.visibility = if (currentStep == onboardingData.size - 1) View.INVISIBLE else View.VISIBLE
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupSystemBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
        }
    }

    override fun onBackPressed() {
        if (currentStep > 0) {
            currentStep--
            updateOnboardingScreen()
        } else {
            super.onBackPressed()
        }
    }
}