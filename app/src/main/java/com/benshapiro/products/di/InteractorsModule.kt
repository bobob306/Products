package com.benshapiro.products.di

import com.benshapiro.products.data.remote.ProductResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// same annotation as the retrofit module
// this is reusable code referring to the productresponsemapper
// product response mapper is specific whereas this can be used as it is elsewhere
// for example if we have more types of data being downloaded in the app
@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {


    @Singleton
    @Provides
    fun provideProductResponseMappper(): ProductResponseMapper {
        return ProductResponseMapper()
    }


}