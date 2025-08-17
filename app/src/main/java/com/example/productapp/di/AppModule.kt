package com.example.productapp.di

import android.content.Context
import androidx.room.Room
import com.example.productapp.data.local.AppDatabase
import com.example.productapp.data.local.FavoriteProductDao
import com.example.productapp.data.repository.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteProductDao {
        return db.favoriteProductDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(dao: FavoriteProductDao): FavoriteRepository {
        return FavoriteRepository(dao)
    }
}
