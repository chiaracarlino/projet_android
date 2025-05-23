package com.epf.android_project.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    @SerializedName("image")
    val image: String,
    val rating: Rating,
    var isFavorite: Boolean = false
)

data class Rating(
    val rate: Double,
    val count: Int
)