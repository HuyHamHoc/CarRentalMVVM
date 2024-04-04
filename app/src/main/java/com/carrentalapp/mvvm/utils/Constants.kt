package com.carrentalapp.mvvm.utils

object Constants {
    val USERNAME_REGEX = Regex("[a-zA-Z0-9]+")
    val USERNAME_LENGTH_REGEX = Regex(".{8,20}")
    val PASSWORD_LENGTH_REGEX = Regex(".{8,20}")
    val PASSWORD_SPECIAL_CHARACTERS_REGEX = Regex("[@#&$!]")
    val PASSWORD_CONTAINS_SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z0-9@#&\$!]")
    val FULLNAME_ALPHABETIC_REGEX = Regex("[a-zA-Z]+")
}
