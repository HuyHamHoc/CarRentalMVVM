package com.carrentalapp.mvvm.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivitySignInBinding
import com.carrentalapp.mvvm.utils.Constants.PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX
import com.carrentalapp.mvvm.utils.Constants.PASSWORD_LENGTH_REGEX
import com.carrentalapp.mvvm.utils.Constants.PASSWORD_SPECIAL_CHARACTERS_REGEX
import com.carrentalapp.mvvm.utils.Constants.USERNAME_LENGTH_REGEX
import com.carrentalapp.mvvm.utils.Constants.USERNAME_REGEX
import com.carrentalapp.mvvm.utils.togglePasswordVisibility


class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString().trim()

                if (userInput.isEmpty()) {
                    binding.edtUsername.error = getString(R.string.username_cannot_empty)
                } else if (!userInput.matches(USERNAME_REGEX.toRegex())) {
                    binding.edtUsername.error = getString(R.string.username_letters_and_numbers)
                } else if (!userInput.matches(USERNAME_LENGTH_REGEX.toRegex())) {
                    binding.edtUsername.error = getString(R.string.username_between_8_20_characters_long)
                } else {
                    binding.edtUsername.error = null
                }
            }
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                if (password.isEmpty()) {
                    binding.edtPassword.error = getString(R.string.password_cannot_empty)
                } else if (!password.matches(PASSWORD_LENGTH_REGEX.toRegex())) {
                    binding.edtPassword.error = getString(R.string.password_between_8_20_characters_long)
                } else if (!password.contains(Regex(PASSWORD_SPECIAL_CHARACTERS_REGEX))) {
                    binding.edtPassword.error = getString(R.string.password_must_character)
                } else if (password.contains(Regex(PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX))) {
                    binding.edtPassword.error = getString(R.string.password_characters)
                } else {
                    binding.edtPassword.error = null
                }
            }
        })

        binding.imageViewEye.setOnClickListener {
            binding.imageViewEye.togglePasswordVisibility(binding.edtPassword)
        }
    }
}