package com.example.flicks.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    var instance: ApiService? = null
        get() {
            if (field == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl(ApiService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                field = retrofit.create(ApiService::class.java)
            }
            return field
        }
        private set
}