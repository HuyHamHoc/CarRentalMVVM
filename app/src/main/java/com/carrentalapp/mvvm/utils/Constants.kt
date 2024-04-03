package com.carrentalapp.mvvm.utils

object Constants {
    const val USERNAME_REGEX = "[a-zA-Z0-9]+"
    const val USERNAME_LENGTH_REGEX = ".{8,20}"
    const val PASSWORD_LENGTH_REGEX = ".{8,20}"
    const val PASSWORD_SPECIAL_CHARACTERS_REGEX = "[@#&$!]"
    const val PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX = "[ ^a-zA-Z0-9@#&\$!]"
}
