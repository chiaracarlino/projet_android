package com.epf.android_project.repository

import com.epf.android_project.api.RetrofitClient
import com.epf.android_project.model.Product
import java.security.Provider

class ProductRepository {
    private val api = RetrofitClient.retrofitInstance.create(Provider.Service::class.java)

    suspend fun getAllProducts(): List<Product> {
        return api.getAllProducts()
    }

    suspend fun getProductById(id: Int): Product {
        return api.getProductById(id)
    }

    suspend fun getCategories(): List<String> {
        return api.getCategories()
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return api.getProductsByCategory(category)
    }
}


