package com.hj.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreEditViewModel : ViewModel() {
    private var _storeEdit = MutableLiveData<Boolean?>(false)
    val storeEdit: LiveData<Boolean?> = _storeEdit

    fun editStore(id: Int, store: Store) {
        StoreApi.storeService.editStore(id, store).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Log.d("스토어 편집", "onResponse: $response")
                if (response.isSuccessful && response.code() == 200) {
                    _storeEdit.value = true
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("스토어 편집", "onFailure: $t")
            }
        })
    }

    fun resetStoreEditState() {
        _storeEdit.value = null
    }
}