package com.example.productapp.repository

import com.example.productapp.data.local.FavoriteProductDao
import com.example.productapp.data.model.FavoriteProduct
import com.example.productapp.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val dao: FavoriteProductDao
) {

    fun getAllFavorites(): Flow<List<FavoriteProduct>> = dao.getAllFavorites()

    suspend fun addToFavorites(product: Product) {
        val favoriteProduct = FavoriteProduct(
            id = product.id,
            title = product.title,
            image = product.image,
            price = product.price,
            brand = product.brand ?: "Unknown"
        )
        dao.insertFavorite(favoriteProduct)
    }

    suspend fun removeFromFavorites(product: Product) {
        val favoriteProduct = FavoriteProduct(
            id = product.id,
            title = product.title,
            image = product.image,
            price = product.price,
            brand = product.brand ?: "Unknown"
        )
        dao.deleteFavorite(favoriteProduct)
    }

    suspend fun isFavorite(productId: Int): Boolean = dao.isFavorite(productId)
}
