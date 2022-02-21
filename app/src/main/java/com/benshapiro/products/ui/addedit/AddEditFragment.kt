package com.benshapiro.products.ui.addedit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.benshapiro.products.databinding.FragmentAddeditBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_addedit.*

@AndroidEntryPoint
class AddEditFragment : Fragment() {

    private var addEditFragmentBinding: FragmentAddeditBinding? = null
    private val binding get() = addEditFragmentBinding!!

    private val addEditViewModel: AddEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addEditFragmentBinding = FragmentAddeditBinding.inflate(inflater, container, false)
        val view = binding.root

        addEditViewModel.product.observe(this.viewLifecycleOwner) { product ->
            product.let {
                binding.apply {
                    nameET.setText(product.name)
                    priceET.setText(product.price.toString())
                    descET.setText(product.description)
                    imageET.setText(product.image)
                }
            }
        }

        binding.saveBtn.setOnClickListener {
            addEditViewModel.saveProduct(
                nameET.text.toString(),
                priceET.text.toString(),
                descET.text.toString(),
                imageET.text.toString()
            )
            val action = AddEditFragmentDirections.actionAddEditFragmentToFragmentList()
            Navigation.findNavController(it).navigate(action)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        addEditFragmentBinding = null
    }

}