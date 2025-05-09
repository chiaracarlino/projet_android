package com.epf.android_project.model

data class Cart(
    val id: Int,
    val userId: Int,
    val date: String,
    val products: List<CartProduct>
)

data class CartProduct(
    val productId: Int,
    val quantity: Int
)