package com.benshapiro.products.ui.detail

import android.graphics.ColorSpace
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.navigation.Navigation
import com.benshapiro.products.R
import com.bumptech.glide.Glide
import com.benshapiro.products.databinding.FragmentDetailBinding
import com.benshapiro.products.model.Model
import com.benshapiro.products.model.getFormattedPrice
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    // binding allows us to access values in the associated xml file
    private var detailFragmentBinding: FragmentDetailBinding? = null
    private val binding get() = detailFragmentBinding!!

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailFragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        detailViewModel.product.observe(this.viewLifecycleOwner) { response ->
            response.let {
                if (response.price != null) {
                    //_product = response
                    initProduct()
                } else {
                    Snackbar.make(
                        binding.detailFragmentCoordinator,
                        "${response.price}",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToAddEditFragment()
            action.model = detailViewModel.product.value
            Navigation.findNavController(it).navigate(action)
        }


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // unbinds on exiting the fragment
        detailFragmentBinding = null
    }

    private fun initProduct() {

        Log.d("View model value", "${detailViewModel.product.value?.id ?: "id not found"}")

        /*
        basically the same as the viewholder in the recyclerview
        just binds the values we have extraced above to the text and image holders
         */
        Glide.with(this.requireContext()).load(detailViewModel.productImage)
            .into(binding.productImage)

        binding.productName.text = detailViewModel.product.value?.name ?: "name not found"
        binding.productPrice.text =
            detailViewModel.product.value?.getFormattedPrice() ?: "price not found"
        binding.productDesc.text = detailViewModel.product.value?.description ?: "desc not found"

        // sets the bookmark image depending on if the bookmark value is 0 or 1
        binding.imageButton.setImageResource(if (detailViewModel.product.value!!.bookmark == 0) {
            R.drawable.ic_baseline_star_border_24 } else {
            R.drawable.ic_baseline_star_24})

        // asks the viewmodel to do a function that updates the database value for bookmark
        binding.imageButton.setOnClickListener{
            detailViewModel.updateBookmarkValue()
        }

    }

}