package com.benshapiro.products.di

import com.benshapiro.products.data.remote.ProductsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// All components below must have the same scope
@Module
@InstallIn(SingletonComponent::class) // installs module in the generated singleton component
object RetrofitModule {

    // this is where all data will come from, the specific file is defined in the data file
    // this makes the code more reusable
    private val BASE_URL = "https://apps-tests.s3-eu-west-1.amazonaws.com/"

    //Singleton as this is the only instance we want in the app
    //Provides means can inject it where needed throughout the application
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): ProductsService {

        return retrofit.create(ProductsService::class.java)

    }

}