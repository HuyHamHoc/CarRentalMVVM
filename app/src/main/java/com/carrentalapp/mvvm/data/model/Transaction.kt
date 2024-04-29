package com.carrentalapp.mvvm.data.model

import java.math.BigDecimal
import java.util.Date

data class Transaction(
    val customerId: String,
    val carId: String,
    val rentalDate: String,
    val returnDate: String,
    val status: String,
    val longitude: String,
    val latitude: String,
    val address: String,
    val totalPrice: BigDecimal
)
