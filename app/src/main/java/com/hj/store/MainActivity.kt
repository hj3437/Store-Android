package com.hj.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hj.store.adapter.OnStoreClickListener
import com.hj.store.adapter.StoreAdapter
import com.hj.store.viewmodel.StoreDetailViewModel
import com.hj.store.viewmodel.StoreViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var storeList: RecyclerView
    private lateinit var storeAdapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storeList = findViewById(R.id.store_list)

        val storeDetailViewModel = ViewModelProvider(this)[StoreDetailViewModel::class.java]

        storeAdapter = StoreAdapter(OnStoreClickListener { store ->
            storeDetailViewModel.setStore(store)
        })

        storeDetailViewModel.store.observe(this) { store ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, StoreDetailFragment.newInstance(store))
                .addToBackStack("fragment transaction")
                .commit()
        }

        val gridLayoutManager = GridLayoutManager(this, 2)
        storeList.apply {
            layoutManager = gridLayoutManager
            adapter = storeAdapter
            hasFixedSize()
        }

        val storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        storeViewModel.store.observe(this) { stores ->
            storeAdapter.submitList(stores)
        }
    }
}