package com.hj.store.remote

import com.hj.store.data.Store
import com.hj.store.data.StoreDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL = "https://dosorme.ga/api/"

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface StoreService {
    @GET("restaurants/")
    fun getRestaurants(): Call<List<Store>>

    // https://dosorme.ga/api/restaurants/1/items
    @GET("restaurants/{store}/items")
    fun getRestaurantItems(@Path("store") store: Int): Call<StoreDetail>
}

object StoreApi {
    val storeService: StoreService by lazy {
        retrofit.create(StoreService::class.java)
    }
}