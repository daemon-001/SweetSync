package com.daemon.sweetsync.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import javax.inject.Inject

class FirebaseClient @Inject constructor() {
    
    // Firebase Authentication instance
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    
    // Firestore database instance
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            // Configure Firestore settings
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Enable offline persistence
                .build()
        }
    }
    
    companion object {
        // Collection names
        const val COLLECTION_USER_PROFILES = "user_profiles"
        const val COLLECTION_BLOOD_SUGAR_READINGS = "blood_sugar_readings"
    }
}
