package com.example.productapp.data.repository

import com.example.productapp.data.api.ApiService
import com.example.productapp.data.model.ProductResponse
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getAllProducts(): Response<List<ProductResponse>> {
        return apiService.getAllProducts()
    }

//    suspend fun getProductById(id: Int): Response<Product> {
//        return apiService.getProductById(id)
//    }
}
