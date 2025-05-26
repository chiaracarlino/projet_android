package com.epf.android_project.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.epf.android_project.databinding.FragmentShopBinding
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.ui.product.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter { product ->
            val action = ShopFragmentDirections.actionShopFragmentToProductFragment(product.id)
            findNavController().navigate(action)
        }

        val category = arguments?.getString("category") ?: ""

        if (category.isNotEmpty()) {
            viewModel.loadProductsByCategory(category)
        } else {
            viewModel.loadAllProducts()
        }

        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { productList ->
                adapter.submitList(productList)

                binding.noResultsText.visibility = if (productList.isEmpty())
                    View.VISIBLE else View.GONE
            }
        }

        binding.shopSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.loadProductsByCategory(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        adapter.onFavoriteClick = {
            viewModel.toggleFavorite(it)
        }

        viewModel.loadAllProducts()
    }
}
