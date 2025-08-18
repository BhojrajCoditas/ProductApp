package com.example.productapp.data.api

import com.example.productapp.data.model.Product
import com.example.productapp.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Get all products
    @GET("products")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    // Get single product by ID (if needed)
//    @GET("products/{id}")
//    suspend fun getProductById(@Path("id") productId: Int): Response<Product>
}
