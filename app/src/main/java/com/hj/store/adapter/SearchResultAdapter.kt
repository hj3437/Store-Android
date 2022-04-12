package com.hj.store.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.store.R
import com.hj.store.data.Store


class SearchResultAdapter(private val clickListener: OnStoreClickListener) :
    ListAdapter<Store, SearchResultAdapter.SearchResultViewHolder>(StoreDiffUtil) {
    class SearchResultViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(store: Store, clickListener: OnStoreClickListener) {
            val storeImageView = rootView.findViewById<ImageView>(R.id.store_list_imageView)

            Glide.with(rootView.context)
                .load(store.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .into(storeImageView)

            val titleTextView = rootView.findViewById<TextView>(R.id.store_list_title_textVIew)
            titleTextView.text = store.name

            val addressTextView = rootView.findViewById<TextView>(R.id.store_list_address_textView)
            addressTextView.text = store.address

            rootView.setOnClickListener {
                clickListener.onClick(store)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.store_list_item, parent, false)
        return SearchResultViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}