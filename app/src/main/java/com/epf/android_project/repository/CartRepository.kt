package com.epf.android_project.repository

import com.epf.android_project.model.CartItem
import com.epf.android_project.model.Product

class CartRepository {

    private val cartItems = mutableListOf<CartItem>()

    fun addProduct(product: Product) {
        val existingItem = cartItems.find { it.productId == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product))
        }
    }

    fun removeProduct(product: Product) {
        cartItems.removeAll { it.productId == product.id }
    }

    fun decreaseQuantity(product: Product) {
        val existingItem = cartItems.find { it.productId == product.id }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                cartItems.remove(existingItem)
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList() // pour éviter les modifications externes
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.product.price * it.quantity }
    }
}
