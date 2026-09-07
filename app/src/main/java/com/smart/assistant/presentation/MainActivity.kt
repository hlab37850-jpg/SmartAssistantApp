package com.smart.assistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.smart.assistant.domain.model.DashboardStats
import com.smart.assistant.presentation.dashboard.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen(
                stats = DashboardStats(),
                onNavigateToCustomers = {},
                onNavigateToInventory = {}
            )
        }
    }
}
