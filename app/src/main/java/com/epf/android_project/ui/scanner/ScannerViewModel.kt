package com.epf.android_project.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.Product
import com.epf.android_project.repository.ProductRepository
import com.epf.android_project.utils.Resource
import kotlinx.coroutines.launch

class ScannerViewModel : ViewModel() {
    private val productRepository = ProductRepository()

    private val _scannedProduct = MutableLiveData<Resource<Product>>()
    val scannedProduct: LiveData<Resource<Product>> = _scannedProduct

    fun processScannedQRCode(qrCodeData: String) {
        viewModelScope.launch {
            try {
                _scannedProduct.value = Resource.Loading()

                val productId = qrCodeData.toIntOrNull()

                if (productId != null) {
                    productRepository.getProductById(productId).collect { result ->
                        _scannedProduct.value = result
                    }
                } else {
                    _scannedProduct.value = Resource.Error("QR code invalide. Format attendu: ID de produit")
                }
            } catch (e: Exception) {
                _scannedProduct.value = Resource.Error("Erreur lors de l'analyse du QR code: ${e.message}")
            }
        }
    }

    fun resetScannedProduct() {
        _scannedProduct.value = null
    }
}