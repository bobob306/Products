package com.benshapiro.products.repository

import androidx.room.withTransaction
import com.benshapiro.products.data.SortOrder
import com.benshapiro.products.data.local.ProductDao
import com.benshapiro.products.data.local.ProductDatabase
import com.benshapiro.products.data.remote.ProductResponseMapper
import com.benshapiro.products.data.remote.ProductsService
import com.benshapiro.products.model.Model
import com.benshapiro.products.utilities.Resource
import com.benshapiro.products.utilities.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
Keeps distance between viewmodel and where the data comes from
Makes code easier to use, maintain and test
Inject gives us the objects previously defined in the di package
 */

class Repository
@Inject
constructor(
    private val productsService: ProductsService,
    private val productResponseMapper: ProductResponseMapper,
    private val productDao: ProductDao,
    private val productDatabase: ProductDatabase,
) {

    /*
    all the logic is already written in networkboundresource
    we just have to call it now
    the query is used in searching for items in the database
    SortOrder is used for changing the order the items are displayed in
    flow is returned so output it continuously being updated
     */

    fun productsRepository(query: String, sortOrder: SortOrder): Flow<Resource<List<Model>>> {

        return networkBoundResource(
            query = {
                productDao.getProducts(query, sortOrder)
            },
            fetch = {
                productResponseMapper.toModelList(productsService.getProducts().products)
            },
            saveFetchResult = {
                /*
                Transaction means all actions are completed or none
                This avoids deleting the the database without inserting anything new
                If insert fails for any reason delete will be undone
                 */
                productDatabase.withTransaction {
                    productDao.deleteAll()
                    productDao.insert(it)
                }
            }
        )

    }


    fun searchDatabase(product: String): Flow<List<Model>> {
        return productDao.searchDatabase(product)
    }


}