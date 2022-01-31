package com.benshapiro.products.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/*
this file allows us to store preferences values when we close and reopen the app
all done on background thread by using flow to avoid ANR error for user
could be in viewmodel, but better to have outside so that the viewmodel is less cluttered
 */

private const val TAG = "Preferences Manager"

// names are in capitals by convention, don't know why
// enum class tells us how many options we have for this class and there can be no more after compile time
enum class SortOrder { BY_NAME, BY_PRICE }

/*
this is the wrapper for the preferences
in this case it is not really needed as there is only one preference, the sort order
but if there were more preferences we could include them with commas in the constructor brackets
 */
data class FilterPreferences(val sortOrder: SortOrder)

// Singleton as we only ever need one instance of this class throughout app
@Singleton
/*
Application context allows us to use it across the whole application
 */
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    // this is the datastore, uses the context defined above and name it something unique
    private val dataStore = context.createDataStore("user_preferences")

    /*
    create flow with current settings
    not private so can be used from outside
    prep the data here so it transformed into a more usable form for our viewmodel
     */
    val preferencesFlow = dataStore.data
            /*
            catch exceptions
            not sure where this would happen because we have a default value
            maybe if there are spelling errors???
            emptyPreferences means we are still able to use the app with default prefs
            Log.e logs the issue in the logcat in the IDE
             */
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
            /*
            map allows us to transform the data as defined in lambda, in this case into a string
            preferences is not necessary, could just use it, preferences is merely more readable
            lambda sets the value of sortOrder using preference keys
            reads current value out of preference keys and if none is currently set use by name as the
            default

             */
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_NAME.name
            )
            FilterPreferences(sortOrder)
        }

    /*
    need suspend because this could be a long running operation so as usual, off the ui thread
    this takes the sortOrder value and stores it in the datastore
    .edit signifies we can make changes
    .name because we cannot store an enum, only a string
    Would need one of these for every type of preference in use e.g. bookmarked items
     */
    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    /*
    you can add more than just one key, we have only one here
    sort_order is the name of how sort order will be stored
    sortOrder is currently an enum but we can only store primitive values here
    Therefore we will transform it into a string
     */
    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
    }
}