package com.example.cpsc411afinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cpsc411afinal.ui.theme.AppRoot
import com.example.cpsc411afinal.ui.theme.CPSC411aFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = AppContainer(applicationContext)

        setContent {
            CPSC411aFinalTheme {
                AppRoot(container)
            }
        }
    }
}