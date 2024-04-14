package com.example.ghusers.di

import com.example.ghusers.repository.api.implementation.UserRepositoryApi
import com.example.ghusers.repository.service.UserService
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
    abstract fun bindUserRepository( api: UserRepositoryApi): UserService
}