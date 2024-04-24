package com.carrentalapp.mvvm.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.CarsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val carsDetailLiveData = MutableLiveData<List<CarsList>>()


    fun loadCarsDetail(id : String) {
        RetrofitHelper.carsDetailService.loadCarsDetail("eq.$id").enqueue(object :
            Callback<List<CarsList>> {
            override fun onResponse(call: Call<List<CarsList>>, response: Response<List<CarsList>>) {
                response.body()?.let {carsLists ->
                    carsDetailLiveData.postValue(carsLists)
                }
            }
            override fun onFailure(call: Call<List<CarsList>>, t: Throwable) {
            }
        })
    }

    fun observerCarsDetailLiveData() : LiveData<List<CarsList>> {
        return carsDetailLiveData
    }
}