package com.hj.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel : ViewModel() {

    private var _stores = MutableLiveData<List<Store>>()
    val store: LiveData<List<Store>> = _stores

    init {
        getStores()
    }

    private fun getStores() {
        StoreApi.storeService.getRestaurants().enqueue(object : Callback<List<Store>> {
            override fun onResponse(
                call: Call<List<Store>>,
                response: Response<List<Store>>
            ) {
                if (response.isSuccessful) {
                    _stores.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // TODO
            }
        })
    }
}