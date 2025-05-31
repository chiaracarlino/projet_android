package com.epf.android_project.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.databinding.FragmentShopBinding
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.utils.FavorisManager

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

        adapter = ProductAdapter(
            onItemClick = { product ->
                val action = ShopFragmentDirections.actionShopFragmentToProductFragment(product.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = {
                refreshProductList()
            }
        )

        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productsRecyclerView.adapter = adapter

        val category = arguments?.getString("category")
        if (!category.isNullOrEmpty()) {
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            shopViewModel.loadProducts(category)
        } else {
            binding.backButton.visibility = View.GONE
            shopViewModel.loadProducts("")
        }

        shopViewModel.filteredProducts.observe(viewLifecycleOwner) {
            refreshProductList()
        }

        binding.shopSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                shopViewModel.filterProducts(newText)
                return true
            }
        })
    }

    private fun refreshProductList() {
        val originalList = shopViewModel.filteredProducts.value ?: return
        val updatedList = originalList.map {
            it.copy(isFavorite = FavorisManager.isFavorite(requireContext(), it.id))
        }
        adapter.submitList(updatedList)
        binding.noResultsText.visibility = if (updatedList.isEmpty()) View.VISIBLE else View.GONE
    }
}


