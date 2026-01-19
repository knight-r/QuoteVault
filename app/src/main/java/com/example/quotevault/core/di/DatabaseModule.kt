package com.example.quotevault.core.di

import android.content.Context
import androidx.room.Room
import com.example.quotevault.data.local.database.QuoteVaultDatabase
import com.example.quotevault.data.local.database.dao.CategoryDao
import com.example.quotevault.data.local.database.dao.CollectionDao
import com.example.quotevault.data.local.database.dao.FavoriteDao
import com.example.quotevault.data.local.database.dao.QuoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuoteVaultDatabase {
        return Room.databaseBuilder(
            context,
            QuoteVaultDatabase::class.java,
            "quotevault_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideQuoteDao(database: QuoteVaultDatabase): QuoteDao {
        return database.quoteDao()
    }

    @Provides
    fun provideCategoryDao(database: QuoteVaultDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideFavoriteDao(database: QuoteVaultDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun provideCollectionDao(database: QuoteVaultDatabase): CollectionDao {
        return database.collectionDao()
    }
}
