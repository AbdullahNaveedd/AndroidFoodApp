package com.example.foodapp.screens.additem

import com.example.foodapp.screens.Constant.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = Constant.BaseUrl
    val api: PostImageApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostImageApi::class.java)
    }
}
