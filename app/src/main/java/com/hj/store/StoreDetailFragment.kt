package com.hj.store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.store.adapter.OnStoreDetailClickListener
import com.hj.store.adapter.StoreDetailAdapter
import com.hj.store.data.Store
import com.hj.store.viewmodel.StoreDetailViewModel

class StoreDetailFragment(private val store: Store) : Fragment() {

    private lateinit var rootView: View
    private lateinit var detailList: RecyclerView
    private lateinit var storeDetailAdapter: StoreDetailAdapter

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 프레그먼트 뒷부분 뷰 클릭방지
        activity?.findViewById<RecyclerView>(R.id.store_list)?.visibility = View.INVISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        // 프레그먼트 뒷부분 뷰 클릭방지 해재
        activity?.findViewById<RecyclerView>(R.id.store_list)?.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storeDetailViewModel = ViewModelProvider(this)[StoreDetailViewModel::class.java]

        storeDetailViewModel.setStore(store)

        val storeImageView = rootView.findViewById<ImageView>(R.id.store_detail_imageView)
        storeImageView.isEnabled = false

        val storeTitleTextView = rootView.findViewById<TextView>(R.id.store_detail_title_textView)
        storeTitleTextView.isClickable = false

        detailList = rootView.findViewById(R.id.store_detail_list)

        storeDetailAdapter = StoreDetailAdapter(OnStoreDetailClickListener {
            Log.d("TAG", "$it")
        })

        val gridLayoutManager = GridLayoutManager(rootView.context, 1)
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
    }
}