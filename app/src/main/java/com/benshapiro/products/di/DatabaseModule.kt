package com.benshapiro.products.di

import android.content.Context
import androidx.room.Room
import com.benshapiro.products.data.local.ProductDao
import com.benshapiro.products.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /*
    This instantiates the database
    Singleton means it is the same instance to be used every time it is needed
    Provides tells Hilt to inject instances of function where called elsewhere in app
    ApplicationContext tells us the instance can be used across the entire application, different contexts can be used to limit the 'scope'
    We say which database we are instantiating in the first bracket
    Fallbackto... enables room to destroy old database if necessary when creating database if migrations are not present when the version number is changed
    Build creates and initializes the database
    */

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ProductDatabase::class.java,
            "table_product",
        ).fallbackToDestructiveMigration()
            .build()
    }

    /*
    This instantiates the database
    Singleton means it is the same instance to be used every time it is needed
    Provides tells Hilt to inject instances of function where called elsewhere in app
     */
    @Singleton
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.getProductDao()
    }


}