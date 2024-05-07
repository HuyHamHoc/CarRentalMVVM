package com.carrentalapp.mvvm.ui.favourite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrentalapp.mvvm.data.datasource.RetrofitHelper
import com.carrentalapp.mvvm.data.model.CarsFavourite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteViewModel : ViewModel() {
    private val carsFavouriteLiveData = MutableLiveData<List<CarsFavourite>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = loadingLiveData
    fun addCarsFavourite(favourite: CarsFavourite) {
        RetrofitHelper.carsFavouriteService.addCarFavourite(favourite)
            .enqueue(object : Callback<CarsFavourite> {
                override fun onResponse(
                    call: Call<CarsFavourite>,
                    response: Response<CarsFavourite>
                ) {
                    if (response.isSuccessful) {
                        val transactions = response.body()
                        transactions?.let {

                        }
                    }
                }

                override fun onFailure(call: Call<CarsFavourite>, t: Throwable) {
                }
            })
    }

    fun getCarsFavourite(customerId: String) {
        loadingLiveData.value = true
        RetrofitHelper.carsFavouriteService.loadCarFavourite("eq.$customerId")
            .enqueue(object : Callback<List<CarsFavourite>> {
                override fun onResponse(
                    call: Call<List<CarsFavourite>>,
                    response: Response<List<CarsFavourite>>
                ) {
                    if (response.isSuccessful) {
                        val transactions = response.body()
                        transactions?.let {
                            carsFavouriteLiveData.postValue(it)
                        }
                    }
                    loadingLiveData.value = false
                }

                override fun onFailure(call: Call<List<CarsFavourite>>, t: Throwable) {
                }
            })
    }
    fun observerCarsFavouriteLiveData() : LiveData<List<CarsFavourite>> {
        return carsFavouriteLiveData
    }

    fun removeCarsFavourite(favouriteId: String) {
        loadingLiveData.value = true
        RetrofitHelper.carsFavouriteService.removeCarFavourite("eq.$favouriteId")
            .enqueue(object : Callback<CarsFavourite> {
                override fun onResponse(
                    call: Call<CarsFavourite>,
                    response: Response<CarsFavourite>)
                { if (response.isSuccessful) {
                    }
                    loadingLiveData.value = false
                }
                override fun onFailure(call: Call<CarsFavourite>, t: Throwable) {
                    // Xử lý lỗi nếu có
                }
            })
    }
}


