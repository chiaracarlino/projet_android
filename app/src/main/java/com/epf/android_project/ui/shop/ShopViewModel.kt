package com.epf.android_project.ui.shop

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.Product
import com.epf.android_project.repository.ProductRepository
import kotlinx.coroutines.launch



class ShopViewModel : ViewModel() {

    private val _allProducts = MutableLiveData<List<Product>>()
    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> = _filteredProducts
    private val repository = ProductRepository()

    fun loadProducts(category: String = "") {
        Log.d("ShopViewModel", "loadProducts called with category: $category")
        viewModelScope.launch {
            try {
                val productList = if (category.isNotEmpty()) {
                    Log.d("ShopViewModel", "Loading products by category")
                    repository.getProductsByCategory(category)
                } else {
                    Log.d("ShopViewModel", "Loading all products")
                    repository.getAllProducts()
                }
                _allProducts.value = productList
                _filteredProducts.value = productList
            } catch (e: Exception) {
                Log.e("ShopViewModel", "Error loading products", e)
            }
        }
    }

    fun filterProducts(query: String?) {
        val products = _allProducts.value ?: return
        _filteredProducts.value = if (query.isNullOrBlank()) {
            products
        } else {
            products.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }
    }

    fun toggleFavorite(product: Product) {
        // Implémente si nécessaire
    }
}

