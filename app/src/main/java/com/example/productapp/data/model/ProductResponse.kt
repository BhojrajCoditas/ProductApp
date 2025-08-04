package com.example.productapp.data.model


data class ProductResponse(
    val status: String,
    val message: String,
    val products: List<Product>
)
