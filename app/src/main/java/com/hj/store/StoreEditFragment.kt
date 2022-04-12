package com.hj.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hj.store.data.Store

class StoreEditFragment(private val store:Store) : Fragment() {

    private lateinit var rootView: View

    companion object {
        fun newInstance(store: Store) = StoreEditFragment(store)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.store_edit_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun finish() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}
