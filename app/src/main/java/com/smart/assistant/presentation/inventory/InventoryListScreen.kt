package com.smart.assistant.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(
    products: List<ProductEntity>,
    onAddProductClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("إدارة المخزون والأصناف") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
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
            items(products) { product ->
                ProductItemCard(product = product)
            }
        }
    }
}

@Composable
private fun ProductItemCard(product: ProductEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "الفئة: ${product.category}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "${product.quantity} ${product.unit}",
                style = MaterialTheme.typography.titleMedium,
                color = if (product.quantity <= product.minQuantity) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
