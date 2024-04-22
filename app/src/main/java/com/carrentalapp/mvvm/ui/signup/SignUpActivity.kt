package com.carrentalapp.mvvm.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.SignUpRequest
import com.carrentalapp.mvvm.databinding.ActivitySignUpBinding
import com.carrentalapp.mvvm.ui.signin.SignInActivity
import com.carrentalapp.mvvm.utils.Constants
import com.carrentalapp.mvvm.utils.togglePasswordVisibility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isFullNameValid = false
    private var isUsernameValid = false
    private var isPasswordValid = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.isEnabled = false


        binding.btnLogin.setOnClickListener {
            val fullName = binding.edtFullname.text.toString()
            val userName = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            signUpUser(fullName, userName, password)
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
                    isFullNameValid = false
                } else if (!fullName.matches(Constants.FULLNAME_ALPHABETIC_REGEX)) {
                    binding.edtFullname.error = getString(R.string.full_name_error)
                    isFullNameValid = false
                } else if (!fullName.matches(Constants.USERNAME_LENGTH_REGEX)) {
                    binding.edtFullname.error = getString(R.string.full_name_error)
                    isFullNameValid = false
                } else {
                    binding.edtFullname.error = null
                    isFullNameValid = true
                }
                updateButtonState()
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
                    isUsernameValid = false
                } else if (!userInput.matches(Constants.USERNAME_REGEX)) {
                    binding.edtUsername.error = getString(R.string.username_letters_and_numbers)
                    isUsernameValid = false
                } else if (!userInput.matches(Constants.USERNAME_LENGTH_REGEX)) {
                    binding.edtUsername.error =
                        getString(R.string.username_between_8_20_characters_long)
                    isUsernameValid = false
                } else {
                    binding.edtUsername.error = null
                    isUsernameValid = true
                }
                updateButtonState()
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
                    isPasswordValid = false
                } else if (password.contains(Constants.PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_characters)
                    isPasswordValid = false
                } else if (!password.matches(Constants.PASSWORD_LENGTH_REGEX)) {
                    binding.edtPassword.error =
                        getString(R.string.password_between_8_20_characters_long)
                    isPasswordValid = false
                } else if (!password.contains(Constants.PASSWORD_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_must_character)
                    isPasswordValid = false
                } else {
                    binding.edtPassword.error = null
                    isPasswordValid = true
                }
                updateButtonState()
            }
        })

        binding.imageViewEye.setOnClickListener {
            binding.imageViewEye.togglePasswordVisibility(binding.edtPassword)
        }
    }

    private fun signUpUser(fullName: String, userName: String, password: String) {

        if (isFullNameValid && isUsernameValid && isPasswordValid) {
            val request = SignUpRequest(fullName, userName, password)

            val call = RetrofitHelper.signUpService.signUp(request)
            call.enqueue(object : Callback<SignUpRequest> {
                override fun onResponse(call: Call<SignUpRequest>, response: Response<SignUpRequest>) {
                    isFullNameValid = false
                    isUsernameValid = false
                    isPasswordValid = false
                }

                override fun onFailure(call: Call<SignUpRequest>, t: Throwable) {
                }
            })
            clearEditTextFields()
        }
    }

    private fun updateButtonState() {
        binding.btnLogin.isEnabled = isFullNameValid && isUsernameValid && isPasswordValid
    }

    private fun clearEditTextFields() {
        binding.edtFullname.text.clear()
        binding.edtUsername.text.clear()
        binding.edtPassword.text.clear()

        binding.edtFullname.error = null
        binding.edtUsername.error = null
        binding.edtPassword.error = null
    }
}
