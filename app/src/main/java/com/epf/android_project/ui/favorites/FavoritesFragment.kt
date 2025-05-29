package com.epf.android_project.ui.favorites

import android.os.Bundle
import android.util.Log
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

    private fun updateFavoriteList() {
        val favoritesIds = FavorisManager.getFavorites(requireContext())
        val favoriteProducts = viewModel.products.value.filter { it.id in favoritesIds }
        adapter.submitList(favoriteProducts)

        binding.emptyTextView.visibility = if (favoriteProducts.isEmpty()) View.VISIBLE else View.GONE
    }

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

        // Action quand on clique sur un produit
        adapter.onItemClick = { product ->
            // Navigue vers la fiche produit
            val action = FavoritesFragmentDirections.actionFavoritesToProductDetail(product.id)
            findNavController().navigate(action)
        }

        // Action quand on clique sur le cœur
        adapter.onFavoriteClick = { product ->
            FavorisManager.toggleFavorite(requireContext(), product.id)

            // On crée une nouvelle liste pour forcer le refresh (évite bug de DiffUtil)
            val favoritesIds = FavorisManager.getFavorites(requireContext())
            val newList = viewModel.products.value
                .filter { p -> FavorisManager.isFavorite(requireContext(), p.id) }
                .map { it.copy(isFavorite = true) }

            adapter.submitList(newList)

            binding.emptyTextView.visibility = if (newList.isEmpty()) View.VISIBLE else View.GONE
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
        adapter.onFavoriteClick = { product ->
            FavorisManager.toggleFavorite(requireContext(), product.id)

            // On met à jour le champ isFavorite localement
            val updatedList = viewModel.products.value.map {
                if (it.id == product.id) it.copy(isFavorite = !it.isFavorite) else it
            }

            val favoritesIds = FavorisManager.getFavorites(requireContext())
            val filteredFavorites = updatedList.filter { it.id in favoritesIds }

            adapter.submitList(filteredFavorites)

            binding.emptyTextView.visibility = if (filteredFavorites.isEmpty()) View.VISIBLE else View.GONE
        }


    }
}


