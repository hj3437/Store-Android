package com.hj.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hj.store.data.Store
import com.hj.store.viewmodel.StoreDetailViewModel

class StoreDetailFragment(private val store: Store) : Fragment() {

    private lateinit var rootView: View

    companion object {
        fun newInstance(store: Store) = StoreDetailFragment(store)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.store_detail_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storeDetailViewModel = ViewModelProvider(this)[StoreDetailViewModel::class.java]
        Log.d("TAG", "$store")
    }
}