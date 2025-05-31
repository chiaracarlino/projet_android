package com.epf.android_project.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.databinding.FragmentFavoritesBinding
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.ui.product.ProductViewModel
import com.epf.android_project.utils.FavorisManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: ProductViewModel by viewModels({ requireActivity() })
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter(
            onItemClick = { product ->
                val action = FavoritesFragmentDirections.actionFavoritesToProductDetail(product.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = {
                refreshFavorites()
            }
        )

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = adapter

        viewModel.loadAllProducts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest {
                refreshFavorites()
            }
        }
    }

    private fun refreshFavorites() {
        val allProducts = viewModel.products.value
        val favoriteList = allProducts
            .filter { FavorisManager.isFavorite(requireContext(), it.id) }
            .map { it.copy(isFavorite = true) }

        adapter.submitList(favoriteList)
        binding.emptyTextView.visibility =
            if (favoriteList.isEmpty()) View.VISIBLE else View.GONE
    }
}




