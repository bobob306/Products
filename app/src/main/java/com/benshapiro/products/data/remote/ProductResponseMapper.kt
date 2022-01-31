package com.benshapiro.products.data.remote

import com.benshapiro.products.model.Model
import com.benshapiro.products.utilities.ModelMapper

class ProductResponseMapper : ModelMapper<ProductsResponse.Product, Model> {

    /*
    returns a model from the entity provided productsresponse
     */
    override fun toModel(entity: ProductsResponse.Product): Model {
        return Model(
            id = entity.id,
            name = entity.name,
            image = entity.image,
            description = entity.description,
            price = entity.price
        )
    }

    // no need for this as we do not need to go from the model to the model
    // it is a required override in the standard code
    override fun fromModel(model: Model): ProductsResponse.Product {
        TODO("Not yet implemented")
    }

    // this gives us a list of models from the list of entities (which are the downloaded products)
    fun toModelList(entity: List<ProductsResponse.Product>): List<Model> {
        return entity.map { toModel(it) }
    }

}