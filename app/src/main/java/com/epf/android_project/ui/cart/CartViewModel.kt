package com.epf.android_project.ui.cart

import androidx.lifecycle.ViewModel
import com.epf.android_project.model.CartItem
import com.epf.android_project.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addProductToCart(product: Product) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentList.add(CartItem(product))
        }

        _cartItems.value = currentList
    }

    fun removeProductFromCart(product: Product) {
        val updatedList = _cartItems.value.filterNot { it.product.id == product.id }
        _cartItems.value = updatedList
    }

    fun decreaseProductQuantity(product: Product) {
        val updatedList = _cartItems.value.toMutableList()
        val item = updatedList.find { it.product.id == product.id }

        if (item != null) {
            if (item.quantity > 1) {
                item.quantity -= 1
            } else {
                updatedList.remove(item)
            }
        }

        _cartItems.value = updatedList
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { cartItem ->
            cartItem.product.price * cartItem.quantity
        }
    }


}
