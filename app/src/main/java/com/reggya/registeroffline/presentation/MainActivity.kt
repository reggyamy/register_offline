package com.reggya.registeroffline.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.reggya.registeroffline.presentation.navigation.RegisterOfflineApp
import com.reggya.registeroffline.presentation.theme.RegisterOfflineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterOfflineTheme {
                Surface {
                    RegisterOfflineApp()
                }
            }
        }
    }
}