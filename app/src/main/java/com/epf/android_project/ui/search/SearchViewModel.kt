package com.epf.android_project.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.Product
import com.epf.android_project.repository.ProductRepository
import com.epf.android_project.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val productRepository = ProductRepository()

    private val _searchResults = MutableLiveData<Resource<List<Product>>>()
    val searchResults: LiveData<Resource<List<Product>>> = _searchResults

    private val _categories = MutableLiveData<Resource<List<String>>>()
    val categories: LiveData<Resource<List<String>>> = _categories

    private val _categoryProducts = MutableLiveData<Resource<List<Product>>>()
    val categoryProducts: LiveData<Resource<List<Product>>> = _categoryProducts

    init {
        loadCategories()
        loadAllProducts()
    }

    private fun loadAllProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { result ->
                _searchResults.value = result
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            productRepository.getCategories().collect { result ->
                _categories.value = result
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = Resource.Loading()
            if (query.isBlank()) {
                loadAllProducts()
            } else {
                productRepository.searchProducts(query).collect { result ->
                    _searchResults.value = result
                }
            }
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(category).collect { result ->
                _categoryProducts.value = result
            }
        }
    }
}
