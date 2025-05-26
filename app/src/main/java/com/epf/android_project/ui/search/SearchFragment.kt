package com.epf.android_project.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.databinding.FragmentSearchBinding
import com.epf.android_project.ui.product.ProductAdapter
import com.epf.android_project.utils.Resource

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter()
        binding.productsRecyclerView.adapter = adapter
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            binding.searchInput.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchInput, InputMethodManager.SHOW_IMPLICIT)
        }, 300)

        binding.searchInput.addTextChangedListener { editable ->
            val query = editable?.toString()?.trim() ?: ""
            viewModel.searchProducts(query)
        }

        binding.searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchInput.text.toString().trim()
                viewModel.searchProducts(query)

                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)

                true
            } else {
                false
            }
        }


        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.noResultsText.visibility = View.GONE
                }
                is Resource.Success -> {
                    val list = resource.data
                    adapter.submitList(list)
                    binding.noResultsText.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.noResultsText.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Erreur: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

