package com.smart.assistant.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.ProductEntity

@Composable
fun InventoryListScreen(
    products: List<ProductEntity> = emptyList()
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(products) { product ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = "الكمية: ${product.quantity} ${product.unit}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
