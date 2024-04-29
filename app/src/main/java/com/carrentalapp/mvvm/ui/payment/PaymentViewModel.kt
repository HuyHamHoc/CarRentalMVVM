package com.carrentalapp.mvvm.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel : ViewModel() {
    private val _carsTransactionLiveData = MutableLiveData<List<Transaction>>()
    val carsTransactionLiveData: LiveData<List<Transaction>> get() = _carsTransactionLiveData

    fun carsTransaction(transaction: Transaction) {
        RetrofitHelper.carsTransactionService.transaction(transaction)
            .enqueue(object : Callback<List<Transaction>> {
                override fun onResponse(
                    call: Call<List<Transaction>>,
                    response: Response<List<Transaction>>
                ) {
                    if (response.isSuccessful) {
                        val transactions = response.body()
                        transactions?.let {
                            _carsTransactionLiveData.postValue(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                }
            })
    }
}