package com.example.productapp.data.api


import com.example.productapp.data.model.Product
import com.example.productapp.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/products")
    suspend fun getAllProducts(): Response<ProductResponse>  // Changed return type

    @GET("api/product/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>  // Get product by ID
}
