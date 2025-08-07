package com.example.productapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.data.local.AppDatabase
import com.example.productapp.data.model.FavoriteProduct
import com.example.productapp.data.model.Product
import com.example.productapp.data.repository.FavoriteRepository
import com.example.productapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository()

    private val favoriteRepository: FavoriteRepository

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    val favoriteProducts: Flow<List<FavoriteProduct>>

    init {
        // Get DAO from AppDatabase
        val dao = AppDatabase.getInstance(application).favoriteProductDao()
        favoriteRepository = FavoriteRepository(dao)

        favoriteProducts = favoriteRepository.getAllFavorites()

        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = productRepository.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let { productResponse ->
                        _productList.value = productResponse.products
                    }
                }
            } catch (e: Exception) {
                // Handle Exception
            }
        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch {
            favoriteRepository.addToFavorites(product)
        }
    }

    fun removeFromFavorites(product: Product) {
        viewModelScope.launch {
            favoriteRepository.removeFromFavorites(product)
        }
    }

    suspend fun isFavorite(productId: Int): Boolean {
        return favoriteRepository.isFavorite(productId)
    }
}
