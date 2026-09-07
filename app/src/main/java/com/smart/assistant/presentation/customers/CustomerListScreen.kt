package com.smart.assistant.presentation.customers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.CustomerEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    customers: List<CustomerEntity>,
    onAddCustomerClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("قائمة العملاء والأرصدة") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCustomerClick) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(customers) { customer ->
                CustomerItemCard(customer = customer)
            }
        }
    }
}

@Composable
private fun CustomerItemCard(customer: CustomerEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = customer.name, style = MaterialTheme.typography.titleMedium)
                Text(text = customer.phone, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "${customer.balance} ر.ي",
                style = MaterialTheme.typography.titleLarge,
                color = if (customer.balance >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
