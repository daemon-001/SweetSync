package com.daemon.sweetsync.di

import com.daemon.sweetsync.data.repository.AuthRepository
import com.daemon.sweetsync.data.repository.BloodSugarRepository
import com.daemon.sweetsync.data.repository.FirebaseClient
import com.google.android.gms.auth.api.identity.SignInClient
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
    fun provideFirebaseClient(): FirebaseClient {
        return FirebaseClient()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseClient: FirebaseClient, oneTapClient: SignInClient): AuthRepository {
        return AuthRepository(firebaseClient, oneTapClient)
    }

    @Provides
    @Singleton
    fun provideBloodSugarRepository(
        firebaseClient: FirebaseClient,
        authRepository: AuthRepository
    ): BloodSugarRepository {
        return BloodSugarRepository(firebaseClient, authRepository)
    }
}
