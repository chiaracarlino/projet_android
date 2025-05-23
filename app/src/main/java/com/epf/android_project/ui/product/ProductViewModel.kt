package com.epf.android_project.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.Product
import com.epf.android_project.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites

    fun toggleFavorite(product: Product) {
        val updatedProduct = product.copy(isFavorite = !product.isFavorite)
        val updatedList = _products.value.map {
            if (it.id == product.id) updatedProduct else it
        }

        _products.value = updatedList
        _favorites.value = updatedList.filter { it.isFavorite }
    }


    fun loadAllProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getAllProducts()
            } catch (e: Exception) {
                // gestion d'erreur ici
            }
        }
    }

    fun loadProductById(id: Int) {
        viewModelScope.launch {
            try {
                _selectedProduct.value = repository.getProductById(id)
            } catch (e: Exception) {
                // gestion d'erreur ici
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = repository.getCategories()
            } catch (e: Exception) {
                // gestion d'erreur ici
            }
        }
    }

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _products.value = repository.getProductsByCategory(category)
            } catch (e: Exception) {
                // gestion d'erreur ici
            }
        }
    }
}
