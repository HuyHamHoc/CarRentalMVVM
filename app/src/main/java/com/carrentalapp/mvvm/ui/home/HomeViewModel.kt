package com.carrentalapp.mvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.CarsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel(){

    private val carsListLiveData = MutableLiveData<List<CarsList>>()
    private val carsListCategoryLiveData = MutableLiveData<List<CarsList>>()


    fun loadCars() {
        RetrofitHelper.carsListService.loadCarsList().enqueue(object : Callback<List<CarsList>>{
            override fun onResponse(call: Call<List<CarsList>>, response: Response<List<CarsList>>) {
                response.body()?.let {carsLists ->
                    carsListLiveData.postValue(carsLists)
                }
            }
            override fun onFailure(call: Call<List<CarsList>>, t: Throwable) {
            }
        })
    }

    fun observerCarsListLiveData() : LiveData<List<CarsList>> {
        return carsListLiveData
    }

    fun loadCarsCategory(categoryId: String) {
        RetrofitHelper.carsCategoryService.loadCarsCategory("eq.$categoryId")
            .enqueue(object : Callback<List<CarsList>> {
                override fun onResponse(
                    call: Call<List<CarsList>>,
                    response: Response<List<CarsList>>
                ) {
                    response.body()?.let { carsLists ->
                        carsListCategoryLiveData.postValue(carsLists)
                    }
                }

                override fun onFailure(call: Call<List<CarsList>>, t: Throwable) {
                }
            })
    }

    fun observerCarsCategoryLiveData(): LiveData<List<CarsList>> {
        return carsListCategoryLiveData
    }
}

