package com.carrentalapp.mvvm.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.carrentalapp.mvvm.databinding.ActivitySignInBinding
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
                    binding.edtUsername.error = "Cannot be empty"
                } else if (!userInput.matches("[a-zA-Z0-9]+".toRegex())) {
                    binding.edtUsername.error = "Can only contain letters and numbers"
                } else if (userInput.length !in 8..20) {
                    binding.edtUsername.error = "Must be between 8-20 characters long"
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
                    binding.edtPassword.error = "Password cannot be empty"
                } else if (password.contains(Regex("[^a-zA-Z0-9@#&$!]"))) {
                    binding.edtPassword.error = "Password contains invalid characters"
                } else if (password.length !in 8..20) {
                    binding.edtPassword.error = "Password must be between 8-20 characters long"
                } else if (!password.contains(Regex("[@#&$!]"))) {
                    binding.edtPassword.error = "Password must contain at least one special character: @, #, &, \$, !"
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