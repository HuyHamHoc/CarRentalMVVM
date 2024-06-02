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
    private val carsTransactionLiveData = MutableLiveData<List<Transaction>>()
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
                            carsTransactionLiveData.postValue(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                }
            })
    }

    fun getTransaction(customerId: String) {
        RetrofitHelper.carsTransactionService.getTransaction("eq.$customerId")
            .enqueue(object : Callback<List<Transaction>> {
                override fun onResponse(
                    call: Call<List<Transaction>>,
                    response: Response<List<Transaction>>
                ) {
                    if (response.isSuccessful) {
                        val transactions = response.body()
                        transactions?.let {
                            carsTransactionLiveData.postValue(it)

                        }
                    }
                }

                override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                }
            })
    }
    fun observerCarsMyBookLiveData() : LiveData<List<Transaction>> {
        return carsTransactionLiveData
    }
}