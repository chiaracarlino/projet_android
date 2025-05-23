package com.epf.android_project.ui.favorites

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.ui.product.ProductViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: ProductViewModel by viewModels({ requireActivity() })
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter()
        adapter.onFavoriteClick = { viewModel.toggleFavorite(it) }

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.favorites.collect {
                adapter.submitList(it)
            }
        }
    }
}
