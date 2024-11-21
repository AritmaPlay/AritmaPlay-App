package com.aritmaplay.app.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aritmaplay.app.R
import com.aritmaplay.app.databinding.ActivitySignUpBinding
import com.aritmaplay.app.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupTextWatchers()

    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edSignupName.text.toString()
            val email = binding.edSignupEmail.text.toString()
            val password = binding.edSignupPassword.text.toString()
            val confirmPassword = binding.edSignupConfirmationPassword.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    getString(R.string.password_mismatch_message),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            Toast.makeText(this,
                "Name: $name Email: $email Password: $password", Toast.LENGTH_SHORT
            ).show()
            goToLoginActivity()
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.edSignupName.addTextChangedListener(textWatcher)
        binding.edSignupEmail.addTextChangedListener(textWatcher)
        binding.edSignupPassword.addTextChangedListener(textWatcher)
        binding.edSignupConfirmationPassword.addTextChangedListener(textWatcher)
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}