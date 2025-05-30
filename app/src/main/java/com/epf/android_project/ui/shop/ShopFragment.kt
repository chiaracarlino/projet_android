package com.epf.android_project.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.databinding.FragmentShopBinding
import com.epf.android_project.ui.product.ProductAdapter

class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private val shopViewModel: ShopViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter { product ->
            val action = ShopFragmentDirections.actionShopFragmentToProductFragment(product.id)
            findNavController().navigate(action)
        }

        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecyclerView.adapter = adapter

        val category = arguments?.getString("category")

        if (!category.isNullOrEmpty()) {
            shopViewModel.loadProducts(category)

            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            shopViewModel.loadProducts("")
            binding.backButton.visibility = View.GONE
        }


        shopViewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
            binding.noResultsText.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }

        val searchView = binding.shopSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                shopViewModel.filterProducts(newText)
                return true
            }
        })

        adapter.onFavoriteClick = {
            shopViewModel.toggleFavorite(it)
        }
    }
}


