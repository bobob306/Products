package com.benshapiro.products.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benshapiro.products.model.Model

/*
Database annotation tells the Hilt what this class is, generating lots of code for us
Abstract because we do not write the implementation, this is generated for us
Not an interface because it extends the RoomDatabase class, interfaces cannot extend classes
First argument 'entities' tells us which table/s are in the database
Version number tells us what version the datbase is, if we change properties of the database you MUST update the version number
Ideally one would migrate the database so you do not lose the values
 */
@Database(entities = [Model::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {

    /*
    This method returns the dao
    Do not need to write the implementation as it is automatically generated
     */
    abstract fun getProductDao(): ProductDao

}