package com.example.productapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: Int,
    val title: String,
    val image: String,
    val price: Double,
    val description: String?,
    val category: String?,
    val brand: String?,
    val model: String?,
    val color: String?,
    val discount: Int?,
    var isFavorite: Boolean = false
) : Parcelable {
    companion object {
        fun fromFavorite(fav: FavoriteProduct): Product {
            return Product(
                id = fav.id,
                title = fav.title,
                image = fav.image,
                price = fav.price,
                description = "No Description Available",
                category = "Unknown Category",
                brand = fav.brand,
                model = "Unknown Model",
                color = "Unknown Color",
                discount = 0,
                isFavorite = true
            )
        }
    }
}
