package com.hj.store

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hj.store.data.Store
import com.hj.store.viewmodel.StoreEditViewModel

class StoreEditActivity : AppCompatActivity() {
    private lateinit var storeEditViewModel: StoreEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_edit)

        storeEditViewModel = ViewModelProvider(this)[StoreEditViewModel::class.java]

        val storeId = intent.getIntExtra("storeId", -1)
        val storeName = intent.getStringExtra("storeName")
        val storeAddress = intent.getStringExtra("storeAddress")
        val storeImageUrl = intent.getStringExtra("storeImageUrl")

        val titleEditText = findViewById<EditText>(R.id.store_edit_title_editTextText)
        titleEditText.setText(storeName)

        val addressEditText = findViewById<EditText>(R.id.store_edit_address_editTextText)
        addressEditText.setText(storeAddress)

        val imageUrlEditText = findViewById<EditText>(R.id.store_edit_imageUrl_editTextText)
        imageUrlEditText.setText(storeImageUrl)

        findViewById<Button>(R.id.store_edit_save_button).setOnClickListener {
            val titleStr = titleEditText.text.toString()
            val addressStr = addressEditText.text.toString()
            val imageUrlStr = imageUrlEditText.text.toString()

            if (titleStr.isNotEmpty() && addressStr.isNotEmpty() && imageUrlStr.isNotEmpty()) {
                // Log.d("스토어 편집", "$titleStr, $addressStr, $imageUrlStr")
                val newStore = Store(
                    id = storeId,
                    name = titleStr,
                    address = addressStr,
                    imageUrl = imageUrlStr
                )
                storeEditViewModel.editStore(storeId, newStore)
            }
        }

        findViewById<Button>(R.id.store_edit_cancel_button).setOnClickListener {
            finish()
        }

        storeEditViewModel.storeEdit.observe(this) { isEditFinish ->
            if (isEditFinish == true) {
                storeEditViewModel.resetStoreEditState()
                finish()
            }
        }
    }
}