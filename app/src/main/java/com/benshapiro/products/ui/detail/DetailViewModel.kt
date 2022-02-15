package com.benshapiro.products.ui.detail

import android.util.Log
import androidx.lifecycle.*
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
) : ViewModel()
{


    val productId = stateHandle.get<Model>("Model")!!.id
    val productName = stateHandle.get<Model>("Model")!!.name
    val productImage = stateHandle.get<Model>("Model")!!.image
    val productPrice = stateHandle.get<Model>("Model")!!.price
    val productDesc = stateHandle.get<Model>("Model")!!.description

    val product = repository.getProductById(productId).asLiveData()
    val check = Log.d("Check product", "${product.value?.id ?: "id not found"}")
}