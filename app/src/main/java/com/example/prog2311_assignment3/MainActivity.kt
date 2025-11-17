package com.example.prog2311_assignment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prog2311_assignment3.feature.ExpenseTracker
import com.example.prog2311_assignment3.ui.theme.PROG2311_Assignment3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PROG2311_Assignment3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpenseTracker(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}