package com.benshapiro.products.di

import com.benshapiro.products.data.remote.ProductsService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/*
A lot of apps use an appcontainer
This unfortunately puts a lot of really important code all together in a way which is harder
to reuse elsewhere in the app
For that reason I have included it here but as you can see it is not being used

In fact, it is commented out, but actually would be fine if you did not comment it out
the whole bit of code does nothing in this app

class AppContainer {

    val retrofit: ProductsService = Retrofit.Builder()
        .baseUrl("https://apps-tests.s3-eu-west-1.amazonaws.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ProductsService::class.java)
}

 */
