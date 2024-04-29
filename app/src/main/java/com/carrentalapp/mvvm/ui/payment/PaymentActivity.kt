package com.carrentalapp.mvvm.ui.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.data.model.Transaction
import com.carrentalapp.mvvm.databinding.ActivityPaymentBinding
import com.carrentalapp.mvvm.ui.detail.DetailViewModel
import com.carrentalapp.mvvm.ui.map.LocationViewModel
import com.carrentalapp.mvvm.ui.map.MapFragment
import com.carrentalapp.mvvm.ui.payment_made.PaymentMadeActivity
import com.carrentalapp.mvvm.utils.Constants
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class PaymentActivity : AppCompatActivity() {
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var viewModel: DetailViewModel
    private var quantity: Int = 0
    private var carPrice: Int = 0
    private var needsDriver: Boolean = false
    private lateinit var locationViewModel: LocationViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        paymentViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]


        binding.btnConfirm.setOnClickListener {
            val startDate = binding.tvStartDate.text.toString()
            val endDate = binding.tvEndDate.text.toString()

            val datePattern = Constants.DATE_FORMAT_REGEX
            val isValidStartDate = startDate.matches(datePattern)
            val isValidEndDate = endDate.matches(datePattern)

            val selectedLocation = binding.tvLocation.text.toString()
            val hasSelectedLocation = selectedLocation.isNotEmpty()

            if (!isValidStartDate || !isValidEndDate) {
                showToast(getString(R.string.select_start_end_date))
            } else if (startDate == endDate) {
                showToast(getString(R.string.start_end_date_same))
            } else if (!hasSelectedLocation) {
                showToast(getString(R.string.select_location))
            } else {
                val transaction = createTransactionObject()
                paymentViewModel.carsTransaction(transaction)
                val totalPrice = calculateTotalPrice()
                val intent = Intent(this@PaymentActivity, PaymentMadeActivity::class.java)
                intent.putExtra("totalPrice", totalPrice.toString())
                startActivity(intent)
            }
        }

        locationViewModel.location.observe(this) { location ->
            val currentLatLong = LatLng(location.latitude, location.longitude)
            placeMarkerOnMap(currentLatLong)
        }


        binding.icLocation.setOnClickListener {
            val mapFragment = MapFragment()
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, mapFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.tvBackDetail.setOnClickListener {
            onBackPressed()
        }

        binding.tvNeedDriver.setOnCheckedChangeListener { _, isChecked ->
            needsDriver = isChecked
            updateDriversFee()
        }

        intent.getStringExtra("carId")?.let { id ->
            viewModel.loadCarsDetail(id)
            viewModel.observerCarsDetailLiveData().observe(this) { carsList ->
                carsList?.firstOrNull()?.let { detail ->
                    with(binding) {
                        tvCarsNameBook.text = detail.name
                        tvCarsPriceBook.text = "$${detail.price}"
                        Glide.with(this@PaymentActivity)
                            .load(detail.picture)
                            .into(imgCarBook)
                        carPrice = detail.price
                        calculateTotalPrice()
                    }
                }
            }
        }

        quantity = intent.getIntExtra("quantity", 0)
        binding.tvSelected.text = quantity.toString()

        binding.tvSelected.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalPrice()
            }
        })

        binding.tvStartDate.setOnClickListener {
            showDatePicker(true)
        }
        binding.tvEndDate.setOnClickListener {
            showDatePicker(false)
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(currentLatLong.latitude, currentLatLong.longitude, 1)
        val address = addresses?.get(0)?.getAddressLine(0)
        binding.tvLocation.text = address
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val selectedDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val formattedDate = selectedDate.timeInMillis.formatDate()
                if (isStartDate) {
                    binding.tvStartDate.text = formattedDate
                } else {
                    binding.tvEndDate.text = formattedDate
                    val startDate = binding.tvStartDate.text.toString()
                    val endDate = binding.tvEndDate.text.toString()
                    val startDateTime = startDate.parseDate()
                    val endDateTime = endDate.parseDate()
                    val diffInMillis = endDateTime - startDateTime
                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                    binding.tvDays.text = diffInDays.toString()
                    calculateTotalPrice()
                }
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun updateDriversFee() {
        binding.tvDriversFee.text = if (needsDriver) "$50" else ""
        calculateTotalPrice()
    }

    @SuppressLint("SetTextI18n")
    private fun calculateTotalPrice(): BigDecimal {
        val numberOfDays = binding.tvDays.text.toString().toIntOrNull() ?: 0
        val carPriceBigDecimal = BigDecimal(carPrice)
        val totalPricePerDay = BigDecimal(100)
        val totalPriceWithoutDriver = carPriceBigDecimal.multiply(BigDecimal(quantity)).add(totalPricePerDay.multiply(
            BigDecimal(numberOfDays)
        ))
        val driversFee = if (needsDriver) BigDecimal(50) else BigDecimal.ZERO
        val totalPrice = totalPriceWithoutDriver.add(driversFee)
        binding.tvPriceBook.text = "$$totalPriceWithoutDriver"
        binding.tvTotal.text = "$$totalPrice"
        return totalPrice
    }

    private fun Long.formatDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(this)
    }

    private fun String.parseDate(): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(this)?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    private fun createTransactionObject(): Transaction {
        val carId = intent.getStringExtra("carId") ?: ""
        val quantity = binding.tvSelected.text.toString().toIntOrNull() ?: 0
        val carPrice = ""
        val rentalDate = binding.tvStartDate.text.toString().parseDateToString()
        val returnDate = binding.tvEndDate.text.toString().parseDateToString()
        val latitude = ""
        val longitude = ""
        val address = binding.tvLocation.text.toString()
        val totalPrice = calculateTotalPrice()

        return Transaction(
            customerId = "",
            carId = carId,
            rentalDate = rentalDate,
            returnDate = returnDate,
            status = "pending",
            longitude = longitude,
            latitude = latitude,
            address = address,
            totalPrice = totalPrice
        )
    }
    private fun String.parseDateToString(): String {
        val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date = inputDateFormat.parse(this)
            outputDateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }
    private fun showToast(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}