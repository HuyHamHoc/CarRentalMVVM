package com.carrentalapp.mvvm.ui.payment_made

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.carrentalapp.mvvm.databinding.ActivityPaymentMadeBinding
import com.carrentalapp.mvvm.ui.payment.PaymentViewModel

class PaymentMadeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentMadeBinding
    private lateinit var bookingViewModel: PaymentViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]

        val totalAmount = intent.getStringExtra("totalPrice")
        binding.tvPricePayment.text = "$$totalAmount"
    }
}
