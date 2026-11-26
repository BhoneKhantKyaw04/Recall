package com.example.recall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recall.ui.RecallApp
import com.example.recall.ui.theme.RecallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as RecallApplication).container
        setContent {
            RecallTheme {
                RecallApp(container)
            }
        }
    }
}