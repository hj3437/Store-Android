package com.hj.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store
import com.hj.store.data.StoreDetail
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreDetailViewModel : ViewModel() {
    private var _store = MutableLiveData<Store>()
    val store: LiveData<Store> get() = _store

    private var _storeDetail = MutableLiveData<StoreDetail>()
    val storeDetail: LiveData<StoreDetail> get() = _storeDetail

    fun setStore(storeInfo: Store) {
        _store.value = storeInfo
        getStoreItems(storeInfo.id)
    }

    private fun getStoreItems(storeId: Int) {
        StoreApi.storeService.getRestaurantItems(storeId).enqueue(object : Callback<StoreDetail> {
            override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                if (response.isSuccessful) {
                    _storeDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<StoreDetail>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}