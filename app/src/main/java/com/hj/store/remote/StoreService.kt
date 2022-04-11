package com.hj.store.remote

import com.hj.store.data.Store
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://dosorme.ga/api/"

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface StoreService {
    @GET("restaurants/")
    fun getRestaurants(): Call<List<Store>>
}

object StoreApi {
    val storeService: StoreService by lazy {
        retrofit.create(StoreService::class.java)
    }
}