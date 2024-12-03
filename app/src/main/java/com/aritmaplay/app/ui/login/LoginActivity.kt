package com.aritmaplay.app.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.aritmaplay.app.MainActivity
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.data.local.pref.UserModel
import com.aritmaplay.app.data.local.pref.UserPreference
import com.aritmaplay.app.data.local.pref.dataStore
import com.aritmaplay.app.databinding.ActivityLoginBinding
import com.aritmaplay.app.ui.signup.SignUpActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupSystemBar()
        observeViewModel()

        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(dataStore)
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        binding.signupButton.setOnClickListener {
            goToSignUpActivity()
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleAnimation = ObjectAnimator.ofFloat(binding.titleLogin, View.ALPHA, 0f, 1f).setDuration(100)
        val emailTextAnimation = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val emailInputAnimation = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordTextAnimation = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordInputAnimation = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val loginButtonAnimation = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(100)
        val signupButtonAnimation = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimation,
                emailTextAnimation,
                emailInputAnimation,
                passwordTextAnimation,
                passwordInputAnimation,
                signupButtonAnimation,
                loginButtonAnimation
            )
            startDelay = 100
        }.start()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { state ->
            when (state) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("LoginActivity", "Logging in...")
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    lifecycleScope.launch {
                        val userPreference = UserPreference.getInstance(dataStore)
                        userPreference.saveSession(
                            UserModel(
                                token = state.data.data.token,
                                isLogin = true
                            )
                        )
                        Log.d("LoginResponse", "Token: ${state.data.data.token}")
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Error: ${state.message}")
                }
            }
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Isi semua data terlebih dahulu ya",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }
    }

    private fun setupSystemBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
        }
    }

    private fun goToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}