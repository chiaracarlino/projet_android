package com.epf.android_project.repository

import com.epf.android_project.api.RetrofitClient
import com.epf.android_project.api.Service
import com.epf.android_project.model.Product

class ProductRepository {

    private val api = RetrofitClient.retrofitInstance.create(Service::class.java)

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

    suspend fun searchProducts(query: String): List<Product> {
        val allProducts = getAllProducts()
        return allProducts.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }
}



