package com.daemon.sweetsync

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.daemon.sweetsync.data.repository.AuthRepository
import com.daemon.sweetsync.data.repository.HealthRepository

class SweetSyncApplication : Application() {

    lateinit var authRepository: AuthRepository
        private set

    lateinit var healthRepository: HealthRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase safely
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApplicationId("1:418483729551:android:2723222e5db08c25906cfa")
                    .setApiKey("AIzaSyBbDkB6oteqDEiFP5Bjcp4XceqWMLMWIh0")
                    .setProjectId("sweet-sync-app")
                    .setStorageBucket("sweet-sync-app.firebasestorage.app")
                    .build()
                FirebaseApp.initializeApp(this, options)
            }
        } catch (_: Exception) {
            // Ignore if already initialized or running in offline mock mode
        }

        authRepository = AuthRepository(this)
        healthRepository = HealthRepository(authRepository)
    }
}
