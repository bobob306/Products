package com.benshapiro.products.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.benshapiro.products.R
import com.benshapiro.products.model.Model
import com.benshapiro.products.model.getFormattedPrice
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.grid_item_layout.view.*

/*
there are a couple of ways to do this list adapter
they all vary very slightly but I hope this reads quite simply
 */

class ListAdapter : RecyclerView.Adapter<ListAdapter.ProductViewHolder>() {

    lateinit var products: List<Model>

    /**
     * this tells the adpater what should be in the recycler view
     * in this case products which are a list of type model
     */
    fun setProduct(products: List<Model>) {
        this.products = products
        notifyDataSetChanged()
    }

    fun createProductList(products: List<Model>) {
        this.products = products
        notifyDataSetChanged()
    }

    // this tells us which item layout to use
    // we have a grid layout
    // could be a row instead
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item_layout, parent, false)
        )
    }

    // this says put the product in corresponding grid item
    // product 0 into item 0, 1 into 1 etc
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    // how many items are there in the list is given answered here
    override fun getItemCount(): Int {
        return products.size
    }

    // this just says what data should be shown in the recyclerview holders
    class ProductViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bind(model: Model) {

            // glide is a tool particularly for loading images
            // glide is particularly good for caching
            Glide.with(itemView).load(model.image).into(itemView.productImage)
            itemView.productName.text = model.name
            itemView.productPrice.text = model.getFormattedPrice()

            // this says what should happen if an item is clicked on
            // in this case you navigate (go to) another fragment and send some data over too
            // the data is the model/product that was clicked on
            itemView.setOnClickListener {

                val action = ListFragmentDirections.actionFragmentListToDetailFragment()
                action.model = model
                Navigation.findNavController(it).navigate(action)

            }

        }

    }

}