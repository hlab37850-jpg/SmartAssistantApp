package com.smart.assistant.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Screen(val title: String, val icon: ImageVector) {
    DASHBOARD("الرئيسية", Icons.Default.Home),
    CUSTOMERS("العملاء", Icons.Default.Person),
    INVENTORY("المخزون", Icons.Default.List),
    SETTINGS("الإعدادات", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartAssistantTheme {
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
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        currentScreen.title, 
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title, fontWeight = FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryAccent,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(paddingValues)
        ) {
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

    Column(modifier = Modifier.padding(20.dp)) {
        Text("نظرة عامة", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("العملاء", "${customers.size}", Icons.Default.Person, PrimaryAccent, Modifier.weight(1f))
            StatCard("المنتجات", "${products.size}", Icons.Default.List, SecondaryAccent, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("المستحقات المالية", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("لك (ديون للعملاء):", color = Color.Gray)
                    Text("$totalDebt ر.ي", color = DebtRed, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("عليك (رصيد مقدم):", color = Color.Gray)
                    Text("$totalCredit ر.ي", color = CreditGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, count: String, icon: ImageVector, accentColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(count, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
            Text(title, color = Color.Gray, fontSize = 14.sp)
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
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryAccent,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة عميل")
            }
        }
    ) { padding ->
        if (customers.isEmpty()) {
            EmptyState("لا يوجد عملاء مضافون حالياً", Icons.Default.Person)
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
                items(customers) { customer ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(customer.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text(customer.phone, color = Color.Gray, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "الرصيد: ${customer.balance} ر.ي",
                                    color = if (customer.balance > 0) DebtRed else CreditGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                            Row {
                                IconButton(onClick = {
                                    val template = settings?.reminderMessageTemplate ?: "تذكير بالسداد:"
                                    val msg = "$template ${customer.balance}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${customer.phone}?text=${Uri.encode(msg)}"))
                                    context.startActivity(intent)
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = "واتساب", tint = SecondaryAccent)
                                }
                                IconButton(onClick = { viewModel.deleteCustomer(customer) }) {
                                    Icon(Icons.Default.Clear, contentDescription = "حذف", tint = DebtRed)
                                }
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
fun InventoryContent(viewModel: MainViewModel) {
    val products by viewModel.products.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryAccent,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة منتج")
            }
        }
    ) { padding ->
        if (products.isEmpty()) {
            EmptyState("المخزون فارغ حالياً", Icons.Default.List)
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
                items(products) { product ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(product.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("الفئة: ${product.category}", color = Color.Gray, fontSize = 13.sp)
                                Text("الكمية: ${product.quantity} ${product.unit}", fontWeight = FontWeight.SemiBold, color = PrimaryAccent)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilledTonalIconButton(
                                    onClick = { viewModel.updateProductQuantity(product, 1) },
                                    shape = RoundedCornerShape(8.dp)
                                ) { Text("+", fontWeight = FontWeight.Bold) }
                                Spacer(modifier = Modifier.width(4.dp))
                                FilledTonalIconButton(
                                    onClick = { viewModel.updateProductQuantity(product, -1) },
                                    shape = RoundedCornerShape(8.dp)
                                ) { Text("-", fontWeight = FontWeight.Bold) }
                                IconButton(onClick = { viewModel.deleteProduct(product) }) {
                                    Icon(Icons.Default.Clear, contentDescription = "حذف", tint = DebtRed)
                                }
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
fun SettingsContent(viewModel: MainViewModel) {
    val settings by viewModel.settings.collectAsState()

    var shopName by remember(settings) { mutableStateOf(settings?.shopName ?: "") }
    var phone by remember(settings) { mutableStateOf(settings?.phone ?: "") }
    var whatsapp by remember(settings) { mutableStateOf(settings?.whatsapp ?: "") }
    var template by remember(settings) { mutableStateOf(settings?.reminderMessageTemplate ?: "") }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("إعدادات المحل", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(value = shopName, onValueChange = { shopName = it }, label = { Text("اسم المحل") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("رقم الواتساب") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = template, onValueChange = { template = it }, label = { Text("قالب رسالة التذكير") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        
        Button(
            onClick = { viewModel.saveSettings(shopName, phone, whatsapp, template) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("حفظ التغييرات", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyState(message: String, icon: ImageVector) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = Color.Gray, fontSize = 16.sp)
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
        title = { Text("إضافة عميل جديد", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم العميل") }, shape = RoundedCornerShape(10.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف") }, shape = RoundedCornerShape(10.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = balance, onValueChange = { balance = it }, label = { Text("الرصيد الأولي") }, shape = RoundedCornerShape(10.dp))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, phone, balance.toDoubleOrNull() ?: 0.0) }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("قطعة") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة منتج جديد", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم المنتج") }, shape = RoundedCornerShape(10.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("الفئة") }, shape = RoundedCornerShape(10.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("الكمية") }, shape = RoundedCornerShape(10.dp))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("الوحدة") }, shape = RoundedCornerShape(10.dp))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, category, quantity.toIntOrNull() ?: 0, unit) }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}
