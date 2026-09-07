package com.smart.assistant.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.domain.model.DashboardStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    stats: DashboardStats,
    onNavigateToCustomers: () -> Unit,
    onNavigateToInventory: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("المساعد الذكي - الرئيسية") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                StatCard(
                    title = "إجمالي أرصدة العملاء (لك)",
                    value = "${stats.totalPositiveBalance} ر.ي",
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
            item {
                StatCard(
                    title = "إجمالي أرصدة العملاء (عليك)",
                    value = "${stats.totalNegativeBalance} ر.ي",
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigateToCustomers,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("إدارة العملاء (${stats.totalCustomers})")
                    }
                    Button(
                        onClick = onNavigateToInventory,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("إدارة المخزون (${stats.totalProducts})")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
