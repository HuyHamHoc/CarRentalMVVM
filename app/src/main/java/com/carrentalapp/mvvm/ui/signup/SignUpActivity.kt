package com.carrentalapp.mvvm.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.ActivitySignUpBinding
import com.carrentalapp.mvvm.ui.signin.SignInActivity
import com.carrentalapp.mvvm.utils.Constants
import com.carrentalapp.mvvm.utils.togglePasswordVisibility

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.edtFullname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val fullName = s.toString().trim()

                if (fullName.isEmpty()) {
                    binding.edtFullname.error = getString(R.string.username_cannot_empty)
                } else if (!fullName.matches(Constants.FULLNAME_ALPHABETIC_REGEX)) {
                    binding.edtFullname.error = getString(R.string.full_name_error)
                } else if (!fullName.matches(Constants.USERNAME_LENGTH_REGEX)) {
                    binding.edtFullname.error = getString(R.string.full_name_error)
                } else {
                    binding.edtFullname.error = null
                }
            }
        })



        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString().trim()

                if (userInput.isEmpty()) {
                    binding.edtUsername.error = getString(R.string.username_cannot_empty)
                } else if (!userInput.matches(Constants.USERNAME_REGEX)) {
                    binding.edtUsername.error = getString(R.string.username_letters_and_numbers)
                }else if (!userInput.matches(Constants.USERNAME_LENGTH_REGEX)) {
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
                } else if (password.contains(Constants.PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_characters)
                } else if (!password.matches(Constants.PASSWORD_LENGTH_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_between_8_20_characters_long)
                } else if (!password.contains(Constants.PASSWORD_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_must_character)
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
