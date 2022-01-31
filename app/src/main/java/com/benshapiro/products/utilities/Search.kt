package com.benshapiro.products.utilities

import androidx.appcompat.widget.SearchView

//inline is more efficient as mentioned in networkboundres
//again crossinline is needed because inside an inline we cannot call return
//without crossinline we cannot compile
//no class as simply an extension function
/*
classname.functionname in this case searchview.onquery...
we pass the listener which is the text being typed into the search bar
This searchView function can be used anywhere we search in the app, currently there is only one place
 */
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    // this refers to the searchview we are acting on
    // object is anonymous class which implements the listener interface
    // the overrides are completely standard and are just implemented by pressing Ctrl I
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            // if this was used it would only work when the submit button is clicked
            // I don't like this way so I just have return true but nothing else!
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // this is triggered every time we type a new letter and immediately filters the list
            // I think this is much sleeker
            listener(newText.orEmpty())
            return true
        }
    })
}