package com.epf.android_project.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)