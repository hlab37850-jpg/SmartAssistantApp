package com.smart.assistant.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.CustomerEntity
import com.smart.assistant.data.local.entity.ProductEntity

enum class Screen(val title: String, val icon: ImageVector) {
    DASHBOARD("الرئيسية", Icons.Default.Home),
    CUSTOMERS("العملاء", Icons.Default.Person),
    INVENTORY("المخزون", Icons.Default.List),
    SETTINGS("الإعدادات", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainAppScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.DASHBOARD -> DashboardContent(viewModel)
                Screen.CUSTOMERS -> CustomerContent(viewModel)
                Screen.INVENTORY -> InventoryContent(viewModel)
                Screen.SETTINGS -> SettingsContent(viewModel)
            }
        }
    }
}

@Composable
fun DashboardContent(viewModel: MainViewModel) {
    val customers by viewModel.customers.collectAsState()
    val products by viewModel.products.collectAsState()

    val totalDebt = customers.filter { it.balance > 0 }.sumOf { it.balance }
    val totalCredit = customers.filter { it.balance < 0 }.sumOf { it.balance }

    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ملخص المحل", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("إجمالي العملاء: ${customers.size}")
                Text("إجمالي المنتجات: ${products.size}")
                Text("إجمالي الديون (لصالحك): $totalDebt ر.ي")
                Text("إجمالي المبالغ (عليك): $totalCredit ر.ي")
            }
        }
    }
}

@Composable
fun CustomerContent(viewModel: MainViewModel) {
    val customers by viewModel.customers.collectAsState()
    val settings by viewModel.settings.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "إضافة عميل")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            items(customers) { customer ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(customer.name, style = MaterialTheme.typography.titleMedium)
                            Text("هاتف: ${customer.phone}", style = MaterialTheme.typography.bodyMedium)
                            Text("الرصيد: ${customer.balance}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            IconButton(onClick = {
                                val template = settings?.reminderMessageTemplate ?: "تذكير بالسداد:"
                                val msg = "$template ${customer.balance}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${customer.phone}?text=${Uri.encode(msg)}"))
                                context.startActivity(intent)
                            }) {
                                Icon(Icons.Default.Share, contentDescription = "واتساب")
                            }
                            IconButton(onClick = { viewModel.deleteCustomer(customer) }) {
                                Icon(Icons.Default.Clear, contentDescription = "حذف")
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddCustomerDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, phone, balance ->
                    viewModel.addCustomer(name, phone, balance)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddCustomerDialog(onDismiss: () -> Unit, onConfirm: (String, String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة عميل جديد") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم العميل") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف") })
                OutlinedTextField(value = balance, onValueChange = { balance = it }, label = { Text("الرصيد الأولي") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, phone, balance.toDoubleOrNull() ?: 0.0) }) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}

@Composable
fun InventoryContent(viewModel: MainViewModel) {
    val products by viewModel.products.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "إضافة منتج")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("الفئة: ${product.category}", style = MaterialTheme.typography.bodySmall)
                            Text("الكمية: ${product.quantity} ${product.unit}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row {
                            Button(onClick = { viewModel.updateProductQuantity(product, 1) }) { Text("+") }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(onClick = { viewModel.updateProductQuantity(product, -1) }) { Text("-") }
                            IconButton(onClick = { viewModel.deleteProduct(product) }) {
                                Icon(Icons.Default.Clear, contentDescription = "حذف")
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddProductDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, category, qty, unit ->
                    viewModel.addProduct(name, category, qty, unit)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("قطعة") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة منتج جديد") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم المنتج") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("الفئة") })
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("الكمية") })
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("الوحدة (كيس/قطعة/حبة)") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, category, quantity.toIntOrNull() ?: 0, unit) }) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}

@Composable
fun SettingsContent(viewModel: MainViewModel) {
    val settings by viewModel.settings.collectAsState()

    var shopName by remember(settings) { mutableStateOf(settings?.shopName ?: "") }
    var phone by remember(settings) { mutableStateOf(settings?.phone ?: "") }
    var whatsapp by remember(settings) { mutableStateOf(settings?.whatsapp ?: "") }
    var template by remember(settings) { mutableStateOf(settings?.reminderMessageTemplate ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = shopName, onValueChange = { shopName = it }, label = { Text("اسم المحل") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("رقم الواتساب") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = template, onValueChange = { template = it }, label = { Text("قالب رسالة التذكير") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.saveSettings(shopName, phone, whatsapp, template) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("حفظ التغييرات")
        }
    }
}
