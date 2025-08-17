package com.example.productapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productapp.data.model.FavoriteProduct

@Database(entities = [FavoriteProduct::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
