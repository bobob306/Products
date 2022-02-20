package com.benshapiro.products.ui.addedit

import android.text.Editable
import androidx.lifecycle.*
import com.benshapiro.products.R
import com.benshapiro.products.model.Model
import com.benshapiro.products.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: Repository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    val productId = stateHandle.get<Model>("Model")?.id ?: "null"

    var product = if (productId != "null") {
        loadProduct(productId)
    } else {
        liveData { Model("", "", R.drawable.ic_baseline_photo_24.toString(), "", 0.0) }
    }

    private fun loadProduct(productId: String): LiveData<Model> {
        return repository.getProductById(productId).asLiveData()
    }

    fun saveProduct(name: String, price: String, desc: String, image: String) {
        /*
        product.value?.name = name ?: "null"
        product.value?.price = price.toDouble() ?: 0.0
        product.value?.description = desc ?: "null"
        product.value?.id = if (productId != "") {
            productId
        } else {
            product.toString()
        }
         */
        if (productId == "null") {
            val currentModel = getNewProductEntry(name, price, desc, image)
            createProduct(currentModel)
        } else {
            val currentModel = getUpdatedProduct(name, price, desc, image)
            updateProduct(currentModel)
        }
    }

    private fun getNewProductEntry(name: String, price: String, desc: String, image: String): Model {
        return Model(
            id = product.toString(),
            name = name,
            image = product.value?.image ?: "R.drawable.ic_baseline_photo_24",
            description = desc,
            price = price.toDouble()
        )
    }

    private fun getUpdatedProduct(name: String, price: String, desc: String, image: String): Model {
        return Model(
            id = productId,
            name = name,
            image = image,
            description = desc,
            price = price.toDouble()
        )
    }

    private fun createProduct(currentModel: Model) {
        viewModelScope.launch {
            repository.insertProduct(currentModel)
        }
    }

    private fun updateProduct(currentModel: Model) {
        viewModelScope.launch {
            repository.updateProduct(currentModel)
        }
    }
}