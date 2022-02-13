package com.benshapiro.products.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.benshapiro.products.model.Model
import com.benshapiro.products.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    private val repository: Repository,
    private val stateHandle: SavedStateHandle
) : ViewModel()
{

    val productId = stateHandle.get<Model>("Model")!!.id
    val productName = stateHandle.get<Model>("Model")!!.name
    val productImage = stateHandle.get<Model>("Model")!!.image
    val productPrice = stateHandle.get<Model>("Model")!!.price
    val productDesc = stateHandle.get<Model>("Model")!!.description

    val product = loadProduct()
    private fun loadProduct() : LiveData<Model> {
        return repository.getProductById(productId).asLiveData()
    }

    val name = product.value?.name ?: "name not found"
    val price = product.value?.price ?: "price not found"
    //val desc = product.value!!.description
    //val image = product.value!!.image

    val check = Log.d("model info =", "$productId, ${product.value?.id ?: "nothing"}, ${product}")

}