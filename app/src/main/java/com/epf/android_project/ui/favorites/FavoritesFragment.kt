package com.epf.android_project.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter()
        adapter.onFavoriteClick = {
            FavorisManager.toggleFavorite(requireContext(), it.id)
            // Pas besoin de refreshFavorites(), car collectLatest observe déjà les changements
        }

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = adapter

        viewModel.loadAllProducts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { products ->
                val favoritesIds = FavorisManager.getFavorites(requireContext())
                val favoriteProducts = products.filter { it.id in favoritesIds }
                adapter.submitList(favoriteProducts)

                Log.d("FavoritesFragment", "Nombre de favoris : ${favoriteProducts.size}")
                binding.emptyTextView.visibility = if (favoriteProducts.isEmpty()) {
                    Log.d("FavoritesFragment", "Aucun favori -> on affiche le message")
                    View.VISIBLE
                } else {
                    Log.d("FavoritesFragment", "Des favoris -> on cache le message")
                    View.GONE
                }
            }
        }
    }
}


