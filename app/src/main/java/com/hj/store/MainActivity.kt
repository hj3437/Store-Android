package com.hj.store

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hj.store.adapter.OnStoreClickListener
import com.hj.store.adapter.StoreAdapter
import com.hj.store.viewmodel.SearchViewModel
import com.hj.store.viewmodel.StoreViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var storeList: RecyclerView
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var searchView: SearchView

    private lateinit var storeViewModel: StoreViewModel
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.store_toolbar)
        setSupportActionBar(toolbar)

        // up 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.watermelon_24x24)

        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        storeList = findViewById(R.id.store_list)

        storeAdapter = StoreAdapter(OnStoreClickListener { store ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, StoreDetailFragment.newInstance(store))
                .addToBackStack(null)
                .commit()
        })

        val gridLayoutManager = GridLayoutManager(this, 2)
        storeList.apply {
            layoutManager = gridLayoutManager
            adapter = storeAdapter
            hasFixedSize()
        }

        storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        storeViewModel.store.observe(this) { stores ->
            storeAdapter.submitList(stores)
        }

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        searchViewModel.searchStore.observe(this) { stores ->
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, StoreSearchResultFragment.newInstance(stores))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.store_menu_item, menu)

        val appBarSearch = menu?.findItem(R.id.app_bar_search)
        searchView = appBarSearch?.actionView as SearchView
        searchView.apply {
            // 서치뷰 width 설정 없을경우 '타이틀...' 보여짐
            maxWidth = Integer.MAX_VALUE
            queryHint = "스토어 검색"
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchStore(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchStore(newText)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.app_bar_login) {
            Log.d("TAG", "Login btn")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard() {
        val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val container = findViewById<ConstraintLayout>(R.id.container)
        keyboard.hideSoftInputFromWindow(container.windowToken, 0)
    }

    private fun searchStore(query: String?) {
        val str = query ?: ""
        if (str.isNotEmpty()) {
            searchViewModel.searchStoreItems(str)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            hideKeyboard()
        } else {
            super.onBackPressed()
        }
    }
}