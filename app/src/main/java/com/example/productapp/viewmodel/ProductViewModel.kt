package com.example.productapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.data.model.FavoriteProduct
import com.example.productapp.data.model.Product
import com.example.productapp.repository.FavoriteRepository
import com.example.productapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    val favoriteProducts: Flow<List<FavoriteProduct>> = favoriteRepository.getAllFavorites()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = productRepository.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let { responseList ->
                        if (responseList.isNotEmpty()) {
                            _productList.value = responseList[0].products
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO: handle error (log/emit state)
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
