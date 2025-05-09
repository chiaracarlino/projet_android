package com.epf.android_project.model

data class CartWithProduct(
    val cartId: Int,
    val items: List<CartItemWithDetails>
)

data class CartItemWithDetails(
    val product: Product,
    val quantity: Int
)