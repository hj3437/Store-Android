package com.hj.store.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.store.R
import com.hj.store.data.Store

class StoreAdapter : ListAdapter<Store, StoreAdapter.StoreViewHolder>(StoreDiffUtil) {
    class StoreViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(item: Store) {
            val storeImageView = rootView.findViewById<ImageView>(R.id.store_list_imageView)

            Glide.with(rootView.context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .into(storeImageView)

            val titleTextView = rootView.findViewById<TextView>(R.id.store_list_title_textVIew)
            titleTextView.text = item.name

            val addressTextView = rootView.findViewById<TextView>(R.id.store_list_address_textView)
            addressTextView.text = item.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.store_list_item, parent, false)
        return StoreViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object StoreDiffUtil : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Store, newItem: Store) = oldItem == newItem
}