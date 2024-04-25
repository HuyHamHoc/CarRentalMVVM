package com.carrentalapp.mvvm.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.LoginResponse
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

        binding.btnSubmit.setOnClickListener {
            val fullName = binding.edtFullname.text.toString()
            val userName = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            if (fullName.isNotBlank() && userName.isNotBlank() && password.isNotBlank() ) {
                signUpUser(fullName, userName, password)
            } else {
                showToast(getString(R.string.is_empty))
            }
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
                    binding.edtFullname.error =
                        getString(R.string.username_between_8_20_characters_long)
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
            // Kiểm tra trạng thái hợp lệ của tên, tên người dùng và mật khẩu
            if (isFullNameValid && isUsernameValid && isPasswordValid) {
                val checkUsernameCall = RetrofitHelper.signInService.signIn()
                checkUsernameCall.enqueue(object : Callback<List<LoginResponse>> {
                    override fun onResponse(
                        call: Call<List<LoginResponse>>,
                        response: Response<List<LoginResponse>>
                    ) {
                        if (response.isSuccessful) {
                            val userList = response.body()
                            val isUsernameExist = userList?.any { it.userName == userName }

                            if (isUsernameExist == true) {
                                showToast(getString(R.string.username_already))
                            } else {
                                proceedWithSignUp(fullName, userName, password)
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                    }
                })
            }
        }


    private fun proceedWithSignUp(fullName: String, userName: String, password: String) {
        val request = SignUpRequest(fullName, userName, password)
        val call = RetrofitHelper.signUpService.signUp(request)
        call.enqueue(object : Callback<SignUpRequest> {
            override fun onResponse(call: Call<SignUpRequest>, response: Response<SignUpRequest>) {
                if (response.isSuccessful) {
                    isFullNameValid = false
                    isUsernameValid = false
                    isPasswordValid = false
                }
            }

            override fun onFailure(call: Call<SignUpRequest>, t: Throwable) {
            }
        })
        showToast(getString(R.string.register_successful))
        clearEditTextFields()
    }


    private fun updateButtonState() {
        binding.btnSubmit.isEnabled = isFullNameValid && isUsernameValid && isPasswordValid
    }

    private fun clearEditTextFields() {
        binding.edtFullname.text.clear()
        binding.edtUsername.text.clear()
        binding.edtPassword.text.clear()

        binding.edtFullname.error = null
        binding.edtUsername.error = null
        binding.edtPassword.error = null
    }

    fun showToast(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}
