package com.example.quotevault.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quotevault.data.local.database.dao.CategoryDao
import com.example.quotevault.data.local.database.dao.CollectionDao
import com.example.quotevault.data.local.database.dao.FavoriteDao
import com.example.quotevault.data.local.database.dao.QuoteDao
import com.example.quotevault.data.local.database.entity.CategoryEntity
import com.example.quotevault.data.local.database.entity.CollectionEntity
import com.example.quotevault.data.local.database.entity.CollectionQuoteEntity
import com.example.quotevault.data.local.database.entity.FavoriteEntity
import com.example.quotevault.data.local.database.entity.QuoteEntity

@Database(
    entities = [
        QuoteEntity::class,
        CategoryEntity::class,
        FavoriteEntity::class,
        CollectionEntity::class,
        CollectionQuoteEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class QuoteVaultDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    abstract fun categoryDao(): CategoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun collectionDao(): CollectionDao
}
