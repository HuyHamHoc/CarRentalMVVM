package com.carrentalapp.mvvm.data.datasource

import com.carrentalapp.mvvm.data.repository.LoginService
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

    val loginService by lazy {
        LoginService.retrofitService(retrofit)
    }
}