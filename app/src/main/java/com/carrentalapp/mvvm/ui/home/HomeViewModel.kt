package com.carrentalapp.mvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.data.model.CategoryModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel(){

    private val carsListLiveData = MutableLiveData<List<CarsList>>()
    private val carsListCategoryLiveData = MutableLiveData<List<CarsList>>()
    private val carsListCategoryGetLiveData = MutableLiveData<List<CategoryModel>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = loadingLiveData

    fun loadCars() {
        loadingLiveData.value = true
        RetrofitHelper.carsListService.loadCarsList().enqueue(object : Callback<List<CarsList>>{
            override fun onResponse(call: Call<List<CarsList>>, response: Response<List<CarsList>>) {
                response.body()?.let {carsLists ->
                    carsListLiveData.postValue(carsLists)
                }
                loadingLiveData.value = false
            }
            override fun onFailure(call: Call<List<CarsList>>, t: Throwable) {
            }
        })
    }

    fun observerCarsListLiveData() : LiveData<List<CarsList>> {
        return carsListLiveData
    }

    fun loadCarsCategory(categoryId: String) {
        loadingLiveData.value = true
        RetrofitHelper.carsCategoryService.loadCarsCategory("eq.$categoryId")
            .enqueue(object : Callback<List<CarsList>> {
                override fun onResponse(
                    call: Call<List<CarsList>>,
                    response: Response<List<CarsList>>
                ) {
                    response.body()?.let { carsLists ->
                        carsListCategoryLiveData.postValue(carsLists)
                    }
                    loadingLiveData.value = false
                }

                override fun onFailure(call: Call<List<CarsList>>, t: Throwable) {
                }
            })
    }

    fun observerCarsCategoryLiveData(): LiveData<List<CarsList>> {
        return carsListCategoryLiveData
    }


    fun loadCarsCategoryList() {
        loadingLiveData.value = true
        RetrofitHelper.carsCategoryService.loadCarsCategoryList()
            .enqueue(object : Callback<List<CategoryModel>> {
                override fun onResponse(
                    call: Call<List<CategoryModel>>,
                    response: Response<List<CategoryModel>>
                ) {
                    response.body()?.let { categoryLists ->
                        carsListCategoryGetLiveData.postValue(categoryLists)
                    }
                    loadingLiveData.value = false
                }

                override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                }
            })
    }

    fun carsListCategoryGetLiveData(): LiveData<List<CategoryModel>> {
        return carsListCategoryGetLiveData
    }
}

