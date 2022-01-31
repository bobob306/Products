package com.benshapiro.products.utilities

/*
Sealed class means no further subclasses can be created after compiling
This means the number of subclasses is known
Perfect for this situation as there are three outcomes for trying to download the data
This is helpful in the IDE as it can automatically create the branches for a when statement and we dont need an else branch
<T> is a generic type which allows us to send any data into the resource
We will pass a list of resources, but we could send anything else
By sending the data/throwable to the supertype it is much easier to use the data in a recyclerview elsewhere
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    /*
    This is for when it works, we pass the data of any type as we are using type T
    This extends the resource class
     */
    class Worked<T>(data: T?) : Resource<T>(data)
    /*
    This is when the work is happening, so we show a progress bar
    The progress bar will show the first time we open the app after installing
    The data is nullable and the default is null, this allows us to decide if we show cached data or an empty list
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
    /*
    throwable allows us to choose what the error says to the user
    This is likely because we do not have internet connectivity#
    Again data is nullable to allow us to show  cached data while showing the error
     */
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}