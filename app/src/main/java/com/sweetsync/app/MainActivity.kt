package com.sweetsync.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sweetsync.app.ui.navigation.SweetSyncApp
import com.sweetsync.app.ui.theme.SweetSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SweetSyncApplication

        setContent {
            SweetSyncTheme {
                SweetSyncApp(
                    authRepository = app.authRepository,
                    healthRepository = app.healthRepository
                )
            }
        }
    }
}
