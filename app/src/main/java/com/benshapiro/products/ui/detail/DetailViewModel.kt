package com.benshapiro.products.ui.detail

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.*
import com.benshapiro.products.R
import com.benshapiro.products.data.PreferencesManager
import com.benshapiro.products.model.Model
import com.benshapiro.products.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    private val repository: Repository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {


    //Use the stateHandle to retrieve info about the product/Model that was clicked on
    //This is possible because all arguments passed to a Fragment or the NavBackStackEntry
    // of a navigation tag is automatically passed to the SavedStateHandle
    val productId = stateHandle.get<Model>("Model")!!.id
    val productName = stateHandle.get<Model>("Model")!!.name
    val productImage = stateHandle.get<Model>("Model")!!.image
    val productPrice = stateHandle.get<Model>("Model")!!.price
    val productDesc = stateHandle.get<Model>("Model")!!.description
    val productBM = stateHandle.get<Model>("Model")!!.bookmark

    //This is not strictly necessary since we actually have all the data above
    //I just prefer to retrieve data from the database and then observe it in frag
    val product = repository.getProductById(productId).asLiveData()

    /*
    Sets a model using the info passed in via stateHandle
    Updates the bookmark value only depending on the current value
    Use a coroutine to launch a suspend fun and update the bookmark value with that model
     */
    fun updateBookmarkValue() {
        val BM = if (product.value!!.bookmark == 0) 1 else 0
        val currentModel = Model (
            id = productId, name = productName, productImage, productDesc, productPrice, BM
                )
        viewModelScope.launch {
            repository.updateProduct(currentModel)
        }
    }
}