package com.example.productapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productapp.data.model.FavoriteProduct
import android.content.Context
import androidx.room.Room



@Database(entities = [FavoriteProduct::class], version = 1, exportSchema = false) // Added exportSchema
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao

    companion object {
        // Volatile annotation ensures that writes to this field are immediately
        // visible to other threads.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // If the instance is not null, return it, otherwise create a new instance
            return INSTANCE ?: synchronized(this) {
                // synchronized block ensures that only one thread can execute this
                // block of code at a time, preventing multiple instances from being created.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Name for your database file
                )
                    // Add migrations if needed, or use fallbackToDestructiveMigration
                    // .fallbackToDestructiveMigration() // Use this only during development if you don't want to handle migrations
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
