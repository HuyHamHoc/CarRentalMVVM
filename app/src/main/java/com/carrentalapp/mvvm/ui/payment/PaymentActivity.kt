package com.carrentalapp.mvvm.ui.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

            // Kiểm tra xem ngày bắt đầu và ngày kết thúc có đúng định dạng "dd/MM/yyyy" không
            val datePattern = Constants.DATE_FORMAT_REGEX
            val isValidStartDate = startDate.matches(datePattern)
            val isValidEndDate = endDate.matches(datePattern)

            // Kiểm tra xem người dùng đã chọn vị trí hay không
            val selectedLocation = binding.tvLocation.text.toString()
            val hasSelectedLocation = selectedLocation.isNotEmpty()

            if ((!isValidStartDate || !isValidEndDate) && !hasSelectedLocation) {
                showToast(getString(R.string.select_date_and_location))
                return@setOnClickListener
            } else if (!hasSelectedLocation && (isValidStartDate || isValidEndDate)) {
                showToast(getString(R.string.select_location))
                return@setOnClickListener
            } else if (hasSelectedLocation && (!isValidStartDate || !isValidEndDate)) {
                showToast(getString(R.string.select_start_end_date))
                return@setOnClickListener
            } else if (hasSelectedLocation && isValidStartDate && isValidEndDate) {
                // Kiểm tra xem ngày kết thúc có lớn hơn ngày bắt đầu không
                val startDateMillis = startDate.parseDateInMillis()
                val endDateMillis = endDate.parseDateInMillis()
                if (endDateMillis <= startDateMillis) {
                    showToast(getString(R.string.end_date_after_start_day))
                    return@setOnClickListener
                }

                // Tạo đối tượng Transaction từ dữ liệu hiện tại của đơn đặt hàng
                val transaction = createTransactionObject()
                // Gọi phương thức carsTransaction và truyền đối tượng Transaction vào
                paymentViewModel.carsTransaction(transaction)
                val totalPrice = calculateTotalPrice()
                if (totalPrice < BigDecimal.ZERO) {
                   showToast(getString(R.string.amount_is_invalid))
                    return@setOnClickListener
                }
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
            binding.prgBar.visibility = View.VISIBLE
            viewModel.observerCarsDetailLiveData().observe(this) { carsList ->
                binding.prgBar.visibility = View.GONE
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
    private fun String.parseDateInMillis(): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(this)?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }
    private fun getCustomerId(): String? {
        // Nhận customerId từ Intent
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("customerId", "")
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
        val carPricePerCar = BigDecimal(carPrice) // Giá tiền cho mỗi chiếc xe
        val totalPricePerCar = carPricePerCar.multiply(BigDecimal(quantity))
        val totalPricePerDay = carPricePerCar // Giá tiền cho mỗi ngày thuê là giá của một chiếc xe
        val totalPriceWithoutDriver = totalPricePerCar.add(totalPricePerDay.multiply(BigDecimal(numberOfDays)))
        val driversFee = if (needsDriver) BigDecimal(50) else BigDecimal.ZERO // Phí tài xế
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
        val customerId = getCustomerId() ?: ""
        val rentalDate = binding.tvStartDate.text.toString().parseDateToString()
        val returnDate = binding.tvEndDate.text.toString().parseDateToString()
        val location = locationViewModel.getLocation()
        val latitude = location?.latitude.toString()
        val longitude = location?.longitude.toString()
        val address = binding.tvLocation.text.toString()
        val totalPrice = calculateTotalPrice()

        return Transaction(
            customerId = customerId,
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