package com.hj.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store

class StoreDetailViewModel : ViewModel() {
    private var _store = MutableLiveData<Store>()
    val store: LiveData<Store> get() = _store

    fun setStore(storeInfo: Store) {
        _store.value = storeInfo
    }
}