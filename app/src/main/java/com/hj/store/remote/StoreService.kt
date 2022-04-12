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
    // 모든 스토어 데이터 호출
    @GET("restaurants/")
    fun getRestaurants(): Call<List<Store>>

    // 특정 스토어 상세화면 데이터 호출
    // https://dosorme.ga/api/restaurants/1/items
    @GET("restaurants/{store}/items")
    fun getRestaurantItems(@Path("store") store: Int): Call<StoreDetail>

    // 스토어 검색
    @GET("restaurants/search/{storeName}")
    fun searchRestaurant(@Path("storeName") storeName: String): Call<List<Store>>
}

object StoreApi {
    val storeService: StoreService by lazy {
        retrofit.create(StoreService::class.java)
    }
}