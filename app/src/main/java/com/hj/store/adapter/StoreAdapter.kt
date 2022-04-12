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
import com.hj.store.MainActivity.Companion.CLICK_MENU_DELETE
import com.hj.store.MainActivity.Companion.CLICK_MENU_EDIT
import com.hj.store.MainActivity.Companion.CLICK_STORE
import com.hj.store.R
import com.hj.store.data.Store


class StoreAdapter(private val clickListener: OnStoreClickListener) :
    ListAdapter<Store, StoreAdapter.StoreViewHolder>(StoreDiffUtil) {
    class StoreViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
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

            val editTextView = rootView.findViewById<TextView>(R.id.store_list_edit_textView)
            editTextView.visibility = View.VISIBLE

            editTextView.setOnClickListener {
                clickListener.onClick(store, CLICK_MENU_EDIT)
            }

            val deleteTextView = rootView.findViewById<TextView>(R.id.store_list_delete_textView)
            deleteTextView.visibility = View.VISIBLE

            deleteTextView.setOnClickListener {
                clickListener.onClick(store, CLICK_MENU_DELETE)
            }

            storeImageView.setOnClickListener {
                clickListener.onClick(store, CLICK_STORE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.store_list_item, parent, false)
        return StoreViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

object StoreDiffUtil : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Store, newItem: Store) = oldItem == newItem
}

class OnStoreClickListener(val clickListener: (store: Store, mode: String) -> Unit) {
    fun onClick(store: Store, mode: String) = clickListener(store, mode)
}