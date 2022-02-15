package com.benshapiro.products.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.benshapiro.products.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    // binding allows us to access values in the associated xml file
    private var detailFragmentBinding: FragmentDetailBinding? = null
    private val binding get() = detailFragmentBinding!!

    private val detailViewModel: DetailViewModel by viewModels()

    // this is a list of the values we will have passed to the fragment when we navigate to it
    // all set to null initially and updated below
    private var name: String? = null
    private var image: String? = null
    private var desc: String? = null
    private var price: Double? = null

    /*
    data is passed in a bundle type (it can be null).
    Bundles are usually used to pass data between activities, these are just fragments, but I wanted
    to include it here just to show a way other than using a sharedviewmodel.
    I would generally use a viewmodel per fragment but here is just another way to go.
    It is then viewed with respect to the values in model.
    Then the four values name image etc are defined using that bundle.
    If there is a problem a snackbar shows that issue, and it is shown in the logcat
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //name = viewModel.name
        //price = viewModel.price
        //desc = viewModel.desc
        //image = viewModel.image


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailFragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        //basically the same as the viewholder in the recyclerview
        //just binds the values we have extraced above to the text and image holders

        Glide.with(this.requireContext()).load(detailViewModel.productImage).into(binding.productImage)


        binding.productName.text = detailViewModel.productName
        binding.productPrice.text = "£${detailViewModel.productPrice.toString()}0"
        binding.productDesc.text = detailViewModel.productDesc

        detailViewModel.check


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // unbinds on exiting the fragment
        detailFragmentBinding = null
    }

}


/*
Original onCreate block
arguments?.let {
            val argument = DetailFragmentArgs.fromBundle(it).model

            if (argument != null) {
                name = argument.name
                image = argument.image
                desc = argument.description
                price = "£${argument.price}0"
            } else {
                Log.d("product_selected", "problem loading selected product")
                Snackbar.make(
                    binding.detailFragmentCoordinator,
                    "Problem loading selected product",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
 */