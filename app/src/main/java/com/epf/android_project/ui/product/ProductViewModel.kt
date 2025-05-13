package com.epf.android_project.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.Product
import com.epf.android_project.repository.CartRepository
import com.epf.android_project.repository.ProductRepository
import com.epf.android_project.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()

    private val _product = MutableLiveData<Resource<Product>>()
    val product: LiveData<Resource<Product>> = _product

    private val _addToCartResult = MutableLiveData<Resource<Boolean>>()
    val addToCartResult: LiveData<Resource<Boolean>> = _addToCartResult

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _product.value = Resource.Loading()
            productRepository.getProductById(productId).collect { result ->
                _product.value = result
            }
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _addToCartResult.value = Resource.Loading()
            cartRepository.addToCart(productId, quantity).collect { result ->
                _addToCartResult.value = result
            }
        }
    }
}
