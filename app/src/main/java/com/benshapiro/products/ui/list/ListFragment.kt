package com.benshapiro.products.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.benshapiro.products.R
import com.benshapiro.products.data.SortOrder
import com.benshapiro.products.databinding.FragmentListBinding
import com.benshapiro.products.utilities.Resource
import com.benshapiro.products.utilities.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    // allows us to use binding with the fragment xml
    private var listFragmentBinding: FragmentListBinding? = null
    private val binding get() = listFragmentBinding!!
    private lateinit var productListAdapter: ListAdapter
    // because we use android entry point we avoid about two lines of code here alone
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // define the adapter for the list
        productListAdapter = ListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // choose the layout and inflate it
        listFragmentBinding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        // observing the livedata defined in the viewmodel
        viewModel.products.observe(this.viewLifecycleOwner) { response ->

            response?.let {

                if (response.data != null) {
                    initRecycler()
                    productListAdapter.createProductList(response.data)
                } else {
                    // this will show the error message if app cannot load data
                    Snackbar.make(binding.main, "${response.error?.message}", Snackbar.LENGTH_LONG)
                        .show()
                }

            }

            // tell us to show progress bar only if state is loading AND there is no data
            binding.progressSpinner.isVisible =
                response is Resource.Loading && response.data.isNullOrEmpty()
        }

        /**
         * need this line to show the menu at the top
         */
        setHasOptionsMenu(true)

        return view
    }


    private fun initRecycler() {
        // the grid items have a fixed size
        binding.productsRecyclerView.setHasFixedSize(true)
        // the grid is two items wide
        binding.productsRecyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        binding.productsRecyclerView.adapter = productListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // this creates it in the R.menu etc is the name of the menu xml file
        inflater.inflate(R.menu.menu, menu)

        // adds the search item to the menu
        // r.id etc references the search in the menu xml file
        val searchItem = menu.findItem(R.id.menu_search)
        // see util.search.kt for more details
        // important functionality to notice is that by using livedata as we type the results change in the ui
        // as casts this normal actionview to a searchview
        // ? means it is nullable, it can be empty
        val searchView = searchItem.actionView as? SearchView

        // kotlin extension function makes this class a lot more concise
        // go to Search.kt
        // we can also use it in other places in the code rather than writing it everytime we use it
        // we only use it once
        searchView?.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    /*
    This is not used because it is better to include search in the sorting functions in this case
    This is how it could be done if there were no sorting functions
     */
    private fun query(query: String) {
        val product = "%$query%"

        viewModel.searchDatabase(product).observe(this, {
            productListAdapter.setProduct(it)
        })
    }

    /**
     * selecting a different option in the menu changes the sortOrder value
     * this is is passed via the viewmodel and repo to the dao
     * the dao then returns the list in the requested order
     * we could do a lot of different things in this option item selected, e.g. delete items that match a condition
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // R.id etc is the name the action is given in the menu xml file
            R.id.action_sort_by_name -> {
                // chooses the sortOrder function in the viewModel and passes the sort order value
                // this automatically updates the repo and dao files
                // this automation means the ui is immediately updated
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_price -> {
                viewModel.onSortOrderSelected(SortOrder.BY_PRICE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // removes the binding, prevents any potential problems with our next binding or rebinding on return to this frag
        listFragmentBinding = null
    }

}