package com.epf.android_project.ui.cart

import androidx.lifecycle.ViewModel
import com.epf.android_project.model.Product
import com.epf.android_project.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _cartItems = MutableStateFlow(repository.getCartItems())
    val cartItems: StateFlow<List<com.epf.android_project.model.CartItem>> = _cartItems

    fun addProductToCart(product: Product) {
        repository.addProduct(product)
        _cartItems.value = repository.getCartItems()
    }

    fun removeProductFromCart(product: Product) {
        repository.removeProduct(product)
        _cartItems.value = repository.getCartItems()
    }

    fun decreaseProductQuantity(product: Product) {
        repository.decreaseQuantity(product)
        _cartItems.value = repository.getCartItems()
    }

    fun clearCart() {
        repository.clearCart()
        _cartItems.value = repository.getCartItems()
    }

    fun getTotalPrice(): Double {
        return repository.getTotalPrice()
    }
}