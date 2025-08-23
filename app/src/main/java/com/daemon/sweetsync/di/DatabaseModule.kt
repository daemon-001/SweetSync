package com.daemon.sweetsync.di

import com.daemon.sweetsync.data.repository.AuthRepository
import com.daemon.sweetsync.data.repository.BloodSugarRepository
import com.daemon.sweetsync.data.repository.SupabaseClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseClient()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(supabaseClient: SupabaseClient): AuthRepository {
        return AuthRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideBloodSugarRepository(
        supabaseClient: SupabaseClient,
        authRepository: AuthRepository
    ): BloodSugarRepository {
        return BloodSugarRepository(supabaseClient, authRepository)
    }
}