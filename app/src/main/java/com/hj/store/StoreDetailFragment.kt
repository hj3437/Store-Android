package com.hj.store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.store.MainActivity.Companion.CLICK_MENU_DELETE
import com.hj.store.MainActivity.Companion.CLICK_MENU_EDIT
import com.hj.store.adapter.OnStoreDetailClickListener
import com.hj.store.adapter.StoreDetailAdapter
import com.hj.store.data.StoreListWithLogin
import com.hj.store.viewmodel.StoreDetailViewModel

class StoreDetailFragment(private val store: StoreListWithLogin) : Fragment() {

    private lateinit var rootView: View
    private lateinit var detailList: RecyclerView
    private lateinit var storeDetailAdapter: StoreDetailAdapter

    private lateinit var storeDetailViewModel: StoreDetailViewModel


    companion object {
        fun newInstance(store: StoreListWithLogin) = StoreDetailFragment(store)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://developer.android.com/guide/fragments/appbar
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.store_detail_fragment, container, false)
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 프레그먼트 뒷부분 뷰 클릭방지
        activity?.findViewById<RecyclerView>(R.id.store_list)?.visibility = View.INVISIBLE

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.store_toolbar)
        toolbar.title = store.name
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
    }

    override fun onDetach() {
        super.onDetach()
        // 프레그먼트 뒷부분 뷰 클릭방지 해재
        activity?.findViewById<RecyclerView>(R.id.store_list)?.visibility = View.VISIBLE

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.store_toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.setNavigationIcon(R.drawable.watermelon_24x24)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeDetailViewModel = ViewModelProvider(this)[StoreDetailViewModel::class.java]

        storeDetailViewModel.setStore(store)

        val storeImageView = rootView.findViewById<ImageView>(R.id.store_detail_imageView)
        storeImageView.isEnabled = false

        val storeTitleTextView = rootView.findViewById<TextView>(R.id.store_detail_title_textView)
        storeTitleTextView.isClickable = false

        detailList = rootView.findViewById(R.id.store_detail_list)

        storeDetailAdapter = StoreDetailAdapter(OnStoreDetailClickListener { item, mode ->
            when (mode) {
                CLICK_MENU_EDIT -> {
                    // TODO
                    Log.d("아이템", "CLICK_MENU_EDIT: $item")
                }
                CLICK_MENU_DELETE -> {
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.ask_user_store_delete))
                        .setPositiveButton(getString(R.string.menu_delete)) { _, _ ->
                            storeDetailViewModel.deleteItem(store.id, item.id)
                        }
                        .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        }.create()
                        .show()
                }
            }
        })

        val linearLayoutManager = LinearLayoutManager(rootView.context)
        detailList.apply {
            layoutManager = linearLayoutManager
            adapter = storeDetailAdapter
            hasFixedSize()
        }

        storeDetailViewModel.store.observe(viewLifecycleOwner) { store ->
            Glide.with(this)
                .load(store.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .into(storeImageView)

            storeTitleTextView.text = store.name
        }

        storeDetailViewModel.storeDetail.observe(viewLifecycleOwner) { storeDetail ->
            storeDetailAdapter.submitList(storeDetail.items)
        }

        storeDetailViewModel.storeItemRemove.observe(viewLifecycleOwner) { isDelete ->
            if (isDelete == true) {
                refreshItem()
                storeDetailViewModel.resetStoreItemRemoveState()
            }
        }
    }

    fun refreshItem() {
        storeDetailViewModel.setStore(store)
    }
}
