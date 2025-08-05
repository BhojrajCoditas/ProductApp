package com.example.productapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.data.model.Product
import com.example.productapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    response.body()?.let { productResponse ->
                        _productList.value = productResponse.products
                    }
                } else {
                    // Handle error response (Optional)
                    // You can log the error or show a message to the user
                }
            } catch (e: Exception) {
                // Handle Exception (Optional)

            }
        }
    }
}
