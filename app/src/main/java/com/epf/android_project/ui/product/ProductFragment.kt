package com.epf.android_project.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.epf.android_project.databinding.FragmentProductBinding
import com.epf.android_project.model.Product
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()

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
            if (product != null) {
                Toast.makeText(requireContext(), "Ajouté au panier", Toast.LENGTH_SHORT).show()
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
