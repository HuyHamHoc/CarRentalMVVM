package com.carrentalapp.mvvm.data.datasource

import com.carrentalapp.mvvm.data.repository.ICarsListService
import com.carrentalapp.mvvm.data.repository.ISignInService
import com.carrentalapp.mvvm.data.repository.ISignUpService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val BASE_URL = "https://kofnjqjkcjtdrnwyayja.supabase.co/rest/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClientInstance.httpClient)
            .build()
    }

    val signInService: ISignInService by lazy {
        retrofit.create(ISignInService::class.java)
    }

    val signUpService: ISignUpService by lazy {
        retrofit.create(ISignUpService::class.java)
    }
    val carsListService: ICarsListService by lazy {
        retrofit.create(ICarsListService::class.java)
    }
}
