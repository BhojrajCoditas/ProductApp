package com.example.productapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val price: Double,
    val brand: String
)