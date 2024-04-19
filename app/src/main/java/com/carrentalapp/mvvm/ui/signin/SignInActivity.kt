package com.carrentalapp.mvvm.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.carrentalapp.mvvm.MainActivity
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper.loginService
import com.carrentalapp.mvvm.data.model.LoginResponse
import com.carrentalapp.mvvm.databinding.ActivitySignInBinding
import com.carrentalapp.mvvm.ui.signup.SignUpActivity
import com.carrentalapp.mvvm.utils.Constants
import com.carrentalapp.mvvm.utils.togglePasswordVisibility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private var mListUser: ArrayList<LoginResponse> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getListUser()

        binding.btnLogin.setOnClickListener {
            clickLogin()
        }
    }

    private fun clickLogin() {
        if (mListUser.isEmpty()) {
            return
        }

        var isHasUser = false
        for (user in mListUser) {
            if ((binding.edtUsername.text.toString() == user.userName) && (binding.edtPassword.text.toString() == user.password)
            ) {
                isHasUser = true
                break
            }
        }
        if (isHasUser) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListUser() {
        loginService.login().enqueue(
            object : Callback<List<LoginResponse>> {
                override fun onResponse(
                    call: Call<List<LoginResponse>>,
                    response: Response<List<LoginResponse>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            mListUser.addAll(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                }
            }
        )

        binding.txtSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewEye.setOnClickListener {
            binding.imageViewEye.togglePasswordVisibility(binding.edtPassword)
        }

        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString().trim()

                if (userInput.isEmpty()) {
                    binding.edtUsername.error = getString(R.string.username_cannot_empty)
                } else if (!userInput.matches(Constants.USERNAME_REGEX)) {
                    binding.edtUsername.error = getString(R.string.username_letters_and_numbers)
                } else if (!userInput.matches(Constants.USERNAME_LENGTH_REGEX)) {
                    binding.edtUsername.error =
                        getString(R.string.username_between_8_20_characters_long)
                } else {
                    binding.edtUsername.error = null
                }
            }
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                if (password.isEmpty()) {
                    binding.edtPassword.error = getString(R.string.password_cannot_empty)
                } else if (!password.matches(Constants.PASSWORD_LENGTH_REGEX)) {
                    binding.edtPassword.error =
                        getString(R.string.password_between_8_20_characters_long)
                } else if (!password.contains(Constants.PASSWORD_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_must_character)
                } else if (password.contains(Constants.PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX)) {
                    binding.edtPassword.error = getString(R.string.password_characters)
                } else {
                    binding.edtPassword.error = null
                }
            }
        })
    }
}