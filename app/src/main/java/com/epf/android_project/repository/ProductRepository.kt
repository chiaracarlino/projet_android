package com.epf.android_project.repository

import com.epf.android_project.api.RetrofitClient
import com.epf.android_project.model.Product
import com.epf.android_project.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

class ProductRepository {
    private val apiService = RetrofitClient.apiService

    fun getAllProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Réponse vide"))
            } else {
                emit(Resource.Error("Erreur: ${response.code()} - ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Erreur réseau"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
    }

    fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getProductById(id)
            emit(handleProductResponse(response))
        } catch (e: IOException) {
            emit(Resource.Error("Erreur réseau"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
    }

    fun searchProducts(query: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    // Filtrer les produits qui contiennent le mot-clé dans le titre ou la description
                    val filteredProducts = products.filter { product ->
                        product.title.contains(query, ignoreCase = true) ||
                                product.description.contains(query, ignoreCase = true) ||
                                product.category.contains(query, ignoreCase = true)
                    }
                    emit(Resource.Success(filteredProducts))
                } ?: emit(Resource.Error("Réponse vide"))
            } else {
                emit(Resource.Error("Erreur: ${response.code()} - ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Erreur réseau"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
    }

    fun getCategories(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Réponse vide"))
            } else {
                emit(Resource.Error("Erreur: ${response.code()} - ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Erreur réseau"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
    }

    fun getProductsByCategory(category: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getProductsByCategory(category)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Réponse vide"))
            } else {
                emit(Resource.Error("Erreur: ${response.code()} - ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Erreur réseau"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
    }

    private fun handleProductResponse(response: Response<Product>): Resource<Product> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Réponse vide")
        } else {
            Resource.Error("Erreur: ${response.code()} - ${response.message()}")
        }
    }
}


