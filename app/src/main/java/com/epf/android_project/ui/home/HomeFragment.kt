package com.epf.android_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.epf.android_project.databinding.FragmentHomeBinding
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.ui.product.ProductListFragment
import com.epf.android_project.ui.product.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnWomen.setOnClickListener {
            navigateToCategory("women's clothing")
        }
        binding.btnMen.setOnClickListener {
            navigateToCategory("men's clothing")
        }
        binding.btnElectronics.setOnClickListener {
            navigateToCategory("electronics")
        }
        binding.btnJewelery.setOnClickListener {
            navigateToCategory("jewelery")
        }
    }

    private fun navigateToCategory(category: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToShopFragment(category)
        findNavController().navigate(action)

    }
}



