package com.smart.assistant.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.domain.model.DashboardStats

@Composable
fun DashboardScreen(
    stats: DashboardStats = DashboardStats()
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("إجمالي العملاء: ${stats.totalCustomers}")
                Text("إجمالي المنتجات: ${stats.totalProducts}")
            }
        }
    }
}
