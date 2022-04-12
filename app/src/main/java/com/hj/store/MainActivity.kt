package com.hj.store

import android.content.Context
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
    private lateinit var toolbar: Toolbar

    private var storeMenu: Menu? = null

    companion object {
        const val USER_LOGIN = 1
        const val USER_GUEST = -1
        const val CLICK_MENU_EDIT = "edit"
        const val CLICK_MENU_DELETE = "delete"
        const val CLICK_STORE = "detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.store_toolbar)
        setSupportActionBar(toolbar)

        // up 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.watermelon_24x24)

        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        storeList = findViewById(R.id.store_list)

        storeAdapter = StoreAdapter(OnStoreClickListener { store, mode ->
            when (mode) {
                CLICK_STORE -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, StoreDetailFragment.newInstance(store))
                        .addToBackStack(null)
                        .commit()
                }
                CLICK_MENU_EDIT -> {
                    Log.d("클릭", "편집")
                }
                CLICK_MENU_DELETE -> {
                    Log.d("클릭", "삭제: ")
                }
            }
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

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (getLoginStatus() == USER_LOGIN) {
            menu?.findItem(R.id.app_bar_login)?.isVisible = false
            menu?.findItem(R.id.app_bar_logout)?.isVisible = true
        } else {
            menu?.findItem(R.id.app_bar_login)?.isVisible = true
            menu?.findItem(R.id.app_bar_logout)?.isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.store_menu_item, menu)

        storeMenu = menu

        if (getLoginStatus() == USER_LOGIN) {
            menu?.findItem(R.id.app_bar_login)?.isVisible = false
            menu?.findItem(R.id.app_bar_logout)?.isVisible = true
            // menu?.findItem(R.id.app_bar_login)?.title = "로그아웃"
        } else {
            menu?.findItem(R.id.app_bar_login)?.isVisible = true
            menu?.findItem(R.id.app_bar_logout)?.isVisible = false
            // menu?.findItem(R.id.app_bar_login)?.title = "로그인"
        }

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
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, StoreLoginFragment.newInstance())
                .addToBackStack(null)
                .commit()
        } else if (item.itemId == R.id.app_bar_logout) {
            setLogout()
            invalidateOptionsMenu()
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

    private fun getLoginStatus(): Int {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getInteger(R.integer.guestuser_default_key)
        return sharedPref.getInt(getString(R.string.saved_user_login_key), defaultValue)
    }

    private fun setLogout() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(getString(R.string.saved_user_login_key), USER_GUEST)
            apply()
        }
    }
}