package com.example.quotevault.core.di

import com.example.quotevault.data.repository.AuthRepositoryImpl
import com.example.quotevault.data.repository.CollectionRepositoryImpl
import com.example.quotevault.data.repository.FavoriteRepositoryImpl
import com.example.quotevault.data.repository.QuoteRepositoryImpl
import com.example.quotevault.data.repository.SettingsRepositoryImpl
import com.example.quotevault.domain.repository.AuthRepository
import com.example.quotevault.domain.repository.CollectionRepository
import com.example.quotevault.domain.repository.FavoriteRepository
import com.example.quotevault.domain.repository.QuoteRepository
import com.example.quotevault.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(impl: CollectionRepositoryImpl): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
