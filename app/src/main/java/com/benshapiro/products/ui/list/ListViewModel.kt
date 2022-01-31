package com.benshapiro.products.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.benshapiro.products.data.PreferencesManager
import com.benshapiro.products.data.SortOrder
import com.benshapiro.products.model.Model
import com.benshapiro.products.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// Tells hilt this is a viewmodel, saves writing some code and tells other places in app what this is
class ListViewModel
@Inject
// Below defined elsewhere is given to the viewmodel by using inject
constructor(
    private val repository: Repository, // in repository file
    private val preferencesManager: PreferencesManager // in preferences manager file
) : ViewModel() {

    /*
    mutablestateflow holds a single value
    can be used as a flow
    initial value needs to be an empty string (unless a very particular circumstance)
    that way nothing is filtered immediately
     */
    val searchQuery = MutableStateFlow("")
    /*
    preferences flow refers to preferences manager which allows us to store preferred sort order when
    we close and reopen the app
     */
    val preferencesFlow = preferencesManager.preferencesFlow

    /*
    Combine means we take both the preferences (the sortorder) and searchQuery into account.
    We just create a pair, but this could be a triple etc.
    Whenever any of the flows mentioned emit a new value it will execute the lambda and pass us all
    latest values
    Hence we must include both values in the lambda and return both latest values in the pair
     */
    val productsFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }
            /*
            sorry quite impenetrable code here all represented by flatmaplatest
            whenever the above flows change (e.g. search changes)
            execute below block
            the parameter is the current value of the flows
            use the value
            run sql query
            assign this result to the productsFlow
            this could be written without the destructing declaration (that is what I have written
            with query, filterpreferences)
            but then it would be really unclear what it.first it.second is actually talking about!
             */
        .flatMapLatest { (query, filterPreferences) ->
            repository.productsRepository(query, filterPreferences.sortOrder)
        }
    /*
    aslivedata internally launches coroutine, collects each value and emits it
    livedata avoids loss of data during configuration change (e.g. rotating device)
     */
    val products = productsFlow.asLiveData()

    /*
    updateSortOrder is a suspend function so need a coroutine
    the scope is just viewmodel, which means it would be cancelled when viewmodel is cleared
    this is sufficient because these are quick functions and will not keep running for a long time
    if that was the case we could use application Scope
     */
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun searchDatabase(product: String): LiveData<List<Model>> {
        return repository.searchDatabase(product).asLiveData()
    }

}