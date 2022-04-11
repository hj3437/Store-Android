package com.hj.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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

        storeDetailViewModel.setStore(store)

        val storeImageView = rootView.findViewById<ImageView>(R.id.store_detail_imageView)
        val storeTitleTextView = rootView.findViewById<TextView>(R.id.store_detail_title_textView)

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
            Log.d("TAG", "$storeDetail")
        }
    }
}