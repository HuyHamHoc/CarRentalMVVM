package com.carrentalapp.mvvm.utils

import android.text.InputType
import android.widget.EditText
import android.widget.ImageView

fun ImageView.togglePasswordVisibility(editTextPassword: EditText) {
    val inputType = editTextPassword.inputType
    editTextPassword.inputType = if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    } else {
        InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    editTextPassword.setSelection(editTextPassword.text.length)
}