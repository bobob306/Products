package com.benshapiro.products.ui.addedit

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

    // For a new product set the Id to null
    // Otherwise retrieve from the SSH
    val productId = stateHandle.get<Model>("Model")?.id ?: "null"

    var product = if (productId != "null") {
        repository.getProductById(productId).asLiveData()
    } else {
        // Create a dummy model with no data
        liveData { Model("", "", R.drawable.ic_baseline_photo_24.toString(), "", 0.0) }
    }

    fun saveProduct(name: String, price: String, desc: String, image: String) {
        /*
        this is saying, is this a new product with a null Id or an existing product
        then either updating or creating a product as is relevant
         */
        val currentModel = getNewProductEntry(name, price, desc, image)
        if (productId == "null") {
            createProduct(currentModel)
        } else {
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

    // coroutines used in the next two because they are needed for a suspend fun
    // suspend fun needed for inserting and updating model in database
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