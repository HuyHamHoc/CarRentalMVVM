package com.carrentalapp.mvvm.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import com.carrentalapp.mvvm.databinding.ActivitySignUpBinding
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
                    binding.edtFullname.error = "Tên không được để trống"
                } else if (!fullName.matches("[a-zA-Z]+".toRegex())) {
                    binding.edtFullname.error = "Kí tự được phép [a-zA-Z]"
                } else if (fullName.length !in 8..20) {
                    binding.edtFullname.error = "Tên phải dài 8-20 ký tự"
                } else {
                    binding.edtFullname.error = null // Clear error if full name is valid
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
                    binding.edtUsername.error = "Không được để trống"
                } else if (!userInput.matches("[a-zA-Z0-9]+".toRegex())) {
                    binding.edtUsername.error = "Chỉ có thể chứa chữ cái và số"
                } else if (userInput.length !in 8..20) {
                    binding.edtUsername.error = "Phải dài 8-20 ký tự"
                } else {
                    binding.edtUsername.error = null // Clear error if username is valid
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
                    binding.edtPassword.error = "Mật khẩu không được để trống"
                } else if (password.contains(Regex("[^a-zA-Z0-9@#&$!]"))) {
                    binding.edtPassword.error = "Mật khẩu chứa ký tự không hợp lệ"
                } else if (password.length !in 8..20) {
                    binding.edtPassword.error = "Mật khẩu phải dài 8-20 ký tự"
                } else if (!password.contains(Regex("[@#&$!]"))) {
                    binding.edtPassword.error = "Mật khẩu phải chứa ít nhất một ký tự đặc biệt: @, #, &, \$, !"
                } else {
                    binding.edtPassword.error = null // Clear error if password is valid
                }
            }
        })



        binding.imageViewEye.setOnClickListener {
            binding.imageViewEye.togglePasswordVisibility(binding.edtPassword)
        }
    }
}
