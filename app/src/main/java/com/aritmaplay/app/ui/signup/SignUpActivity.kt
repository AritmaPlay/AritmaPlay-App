package com.aritmaplay.app.ui.signup

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
import androidx.core.view.WindowCompat
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.databinding.ActivitySignUpBinding
import com.aritmaplay.app.ui.login.LoginActivity
import com.aritmaplay.app.data.Result

class SignUpActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupTextWatchers()
        setupSystemBar()
        observeViewModel()
        playAnimation()
    }

    private fun observeViewModel() {
        viewModel.signupResult.observe(this) { state ->
            when (state) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("SignUpActivity", "Loading user data...")
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("SignUpActivity", "Success: User data fetched successfully!")
                    goToLoginActivity()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    Log.e("SignUpActivity", "Error: ${state.message}")
                }
            }
        }
    }

    private fun playAnimation() {
        val titleAnimation = ObjectAnimator.ofFloat(binding.titleLogin, View.ALPHA, 0f, 1f).setDuration(100)
        val nameTextAnimation = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val nameInputAnimation = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val userNameTextAnimation = ObjectAnimator.ofFloat(binding.userNameTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val userNameInputAnimation = ObjectAnimator.ofFloat(binding.userNameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val emailTextAnimation = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val emailInputAnimation = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordTextAnimation = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordInputAnimation = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val confirmPasswordTextAnimation = ObjectAnimator.ofFloat(binding.passwordConfirmationTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val confirmPasswordInputAnimation = ObjectAnimator.ofFloat(binding.passwordConfirmationEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val signupButtonAnimation = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimation,
                nameTextAnimation,
                nameInputAnimation,
                userNameTextAnimation,
                userNameInputAnimation,
                emailTextAnimation,
                emailInputAnimation,
                passwordTextAnimation,
                passwordInputAnimation,
                confirmPasswordTextAnimation,
                confirmPasswordInputAnimation,
                signupButtonAnimation
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edSignupName.text.toString()
            val userName = binding.edSignupUserName.text.toString()
            val email = binding.edSignupEmail.text.toString()
            val password = binding.edSignupPassword.text.toString()
            val confirmPassword = binding.edSignupConfirmationPassword.text.toString()

            if (name.isEmpty() || userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    "Isi semua data terlebih dahulu ya",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    getString(R.string.password_mismatch_message),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.register(name, userName, email, password, "https://api.dicebear.com/9.x/thumbs/svg")
        }
    }

    private fun setupSystemBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.edSignupName.addTextChangedListener(textWatcher)
        binding.edSignupUserName.addTextChangedListener(textWatcher)
        binding.edSignupEmail.addTextChangedListener(textWatcher)
        binding.edSignupPassword.addTextChangedListener(textWatcher)
        binding.edSignupConfirmationPassword.addTextChangedListener(textWatcher)
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}