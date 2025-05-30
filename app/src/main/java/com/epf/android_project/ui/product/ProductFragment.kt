package com.epf.android_project.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.epf.android_project.databinding.FragmentProductBinding
import com.epf.android_project.model.Product
import com.epf.android_project.ui.cart.CartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epf.android_project.R


class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ProductFragmentArgs.fromBundle(requireArguments())
        val productId = args.productId

        val toolbar = view.findViewById<Toolbar>(R.id.product_toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (productId != -1) {
            viewModel.loadProductById(productId)
        } else {
            Toast.makeText(requireContext(), "Produit non trouvé", Toast.LENGTH_SHORT).show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedProduct.collectLatest { product ->
                if (product != null) {
                    showProduct(product)
                }
            }
        }

        binding.addToCartButton.setOnClickListener {
            val product = viewModel.selectedProduct.value
            val quantity = binding.quantityEditText.text.toString().toIntOrNull() ?: 1

            if (product != null) {
                repeat(quantity) {
                    cartViewModel.addProductToCart(product)
                }
                Toast.makeText(requireContext(), "$quantity ajouté(s) au panier", Toast.LENGTH_SHORT).show()
            }


        }


        binding.increaseQuantityButton.setOnClickListener {
            val current = binding.quantityEditText.text.toString().toIntOrNull() ?: 1
            binding.quantityEditText.setText((current + 1).toString())
        }

        binding.decreaseQuantityButton.setOnClickListener {
            val current = binding.quantityEditText.text.toString().toIntOrNull() ?: 1
            if (current > 1) {
                binding.quantityEditText.setText((current - 1).toString())
            }
        }


    }

    private fun showProduct(product: Product) {
        binding.productTitle.text = product.title
        binding.productPrice.text = "$${product.price}"
        binding.productDescription.text = product.description

        Glide.with(binding.productImage.context)
            .load(product.image)
            .centerCrop()
            .into(binding.productImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
