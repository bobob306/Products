package com.benshapiro.products.data.remote

import retrofit2.http.GET

interface ProductsService {
/*
This is a get which goes to the particular network data file.
By only putting that here we can reuse all the other code.
Suspend fun because it is a network operation so could be long lasting and
should not be on main thread to avoid anr
 */

    @GET("android/products.json")
    suspend fun getProducts(): ProductsResponse
}
