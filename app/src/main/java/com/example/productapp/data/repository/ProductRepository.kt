package com.example.productapp.data.repository


import com.example.productapp.data.api.ApiClient
import com.example.productapp.data.model.Product
import com.example.productapp.data.model.ProductResponse
import retrofit2.Response

class ProductRepository {

    suspend fun getAllProducts(): Response<ProductResponse> {
        return ApiClient.apiService.getAllProducts()
    }

    suspend fun getProductById(id: Int): Response<Product> {
        return ApiClient.apiService.getProductById(id)
    }
}
