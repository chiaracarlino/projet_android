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
            try {
                _searchResults.value = Resource.Loading()
                val products = productRepository.getAllProducts()
                _searchResults.value = Resource.Success(products)
            } catch (e: Exception) {
                _searchResults.value = Resource.Error(e.message ?: "Erreur inconnue")
            }
        }
    }


    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = Resource.Loading()
                val cats = productRepository.getCategories()
                _categories.value = Resource.Success(cats)
            } catch (e: Exception) {
                _categories.value = Resource.Error(e.message ?: "Erreur inconnue")
            }
        }
    }


    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                _searchResults.value = Resource.Loading()
                val products = if (query.isBlank()) {
                    productRepository.getAllProducts()
                } else {
                    productRepository.searchProducts(query)
                }
                _searchResults.value = Resource.Success(products)
            } catch (e: Exception) {
                _searchResults.value = Resource.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _categoryProducts.value = Resource.Loading()
                val products = productRepository.getProductsByCategory(category)
                _categoryProducts.value = Resource.Success(products)
            } catch (e: Exception) {
                _categoryProducts.value = Resource.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

}
