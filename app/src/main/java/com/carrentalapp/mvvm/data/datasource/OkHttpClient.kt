package com.carrentalapp.mvvm.data.datasource

import okhttp3.OkHttpClient


object OkHttpClientInstance {
    var httpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.addNetworkInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZm5qcWprY2p0ZHJud3lheWphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTE5NDE4MjMsImV4cCI6MjAyNzUxNzgyM30.W7vfBSHwDT7_Qxz17HuwmeZY8td8Ue98d6GXDq7nAHc")
            chain.proceed(requestBuilder.build())
        }
        httpClient = builder.build()
    }
}



