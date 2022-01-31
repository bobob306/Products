package com.benshapiro.products.utilities

import kotlinx.coroutines.flow.*
/*
This  picture should help to explain this file a lot better than just looking at all this text!
https://hsto.org/webt/e1/wv/c9/e1wvc9un_w2t8jyi0kiky8wagw0.png
*/
/*
This file works hand in hand with the resource file below
When reading through try to have both open side by side
 */

/*
This is a file not a class because it is actually just a function
Each crossinlinefun has a different responsibility
Could be done in one go, but much better to separate out
This is fully reusable as we could later use different function arguments depending on what we are passing
ResultType and RequestType allow us to reuse it with different types of data
Result and Request are not necessarily the same (in our case they are)
It is possible we could make changes to the data before storing it in the database so it is better to have two types
Inline is more efficient and better practise, not necessary
Inlinefun doesn't create unnecessary objects when compiling code, just copies them into the place where they are called
Crossin means we cannot call return inside those functions (that would break the upper function)

 */
inline fun <ResultType, RequestType> networkBoundResource(
    // Responsibility for loading data from room database
    // : declares type
    // () are empty as function has no input params
    // only returns something, which is a Flow mentioned in Dao, continuously updated when database changes
    crossinline query: () -> Flow<ResultType>,
    // Responsibility for deciding if data in database needs updating
    // suspend fun because network functions can take a long time, so we keep it off the main thread
    crossinline fetch: suspend () -> RequestType,
    // Responsibility for saving data from rest api into room database
    // Again a suspend as editing the database is a long running action
    // Nothing returned, just stores data in database so return type is unit
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    // Responsibility for loading new data from rest api
    // Doesn't need whole flow, just one piece of data is enough here
    // By setting it to true it will always update the cache when the app is opened
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    /*
    in a flow we can have long running operations
    this block will be run whenever networkboundres is called
    nothing will be executed until we collect from it, no need to suspend at this stage
    first() is a suspend function, that is allowed because we are in a flow block
    first() just returns the first value from the flow, we just get one list of restaurants
    each branch will return the different resource types
    the if block decides if we should get new data, by default is defined as true above
    when true we must start fetching data
    try catch allows us get a throwable
     */
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        /*
        having the data here means we can show the cached data while loading the new data
        null being passed would mean the ui is empty, either work fine but null is often less helpful in offline conditions
        emit shows values from this flow, in this case it tells us to show the progress bar
         */
        emit(Resource.Loading(data))

        /*
        try catch block is needed because although we want to load data we may not have an internet connection
        no internet connection means it will fail
        we want to throw the error
        query used so we can show the flow in the app if anything changes
        it in this case means a list of restaurants because that is defined in the query flow
        now room just adds it to the database
         */
        try {
            saveFetchResult(fetch())
            query().map { Resource.Worked(it) }
        } catch (throwable: Throwable) {
            // this still shows data in the ui, but it is the old data, the error tells us we could not download the new data
            query().map { Resource.Error(throwable, it) }
        }
    /*
    else block for when we do not need to update the data because it is already up to date
     */
    } else {
        query().map { Resource.Worked(it) }
    }

    /*
    at the end we just emitAll whatever the value of the flow variable is
    this saves a lot of typing emit all over the functions
    apologies this may not be well explained, the flowchart hopefully illustrates some of this very wordy explanation
     */
    emitAll(flow)

}
