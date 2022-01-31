package com.benshapiro.products.data.remote

data class ProductsResponse(
    val products: List<Product> = emptyList()) {
//these are fields the incoming data will map to
    data class Product(
        val id: String,
        val name: String,
        val image: String,
        val description: String,
        val price: Double,
    )
}
