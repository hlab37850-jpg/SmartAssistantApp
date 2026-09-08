package com.smart.assistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.entity.CustomerEntity
import com.smart.assistant.data.entity.ProductEntity
import com.smart.assistant.data.entity.SettingsEntity

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val customers by viewModel.customers.collectAsState()
    val products by viewModel.products.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerBalance by remember { mutableStateOf("") }

    var productName by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productUnit by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }

    var shopName by remember { mutableStateOf(settings?.shopName ?: "") }
    var ownerName by remember { mutableStateOf(settings?.ownerName ?: "") }
    var phone by remember { mutableStateOf(settings?.phone ?: "") }
    var whatsapp by remember { mutableStateOf(settings?.whatsapp ?: "") }
    var address by remember { mutableStateOf(settings?.address ?: "") }
    var reminderMessageTemplate by remember { mutableStateOf(settings?.reminderMessageTemplate ?: "") }

    LaunchedEffect(settings) {
        settings?.let {
            shopName = it.shopName
            ownerName = it.ownerName
            phone = it.phone
            whatsapp = it.whatsapp
            address = it.address
            reminderMessageTemplate = it.reminderMessageTemplate
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (shopName.isNotEmpty()) shopName else "مدير المحل والمستحقات الذكي") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("العملاء") },
                    label = { Text("العملاء") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Text("المخزن") },
                    label = { Text("المخزن") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Text("الإعدادات") },
                    label = { Text("الإعدادات") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("اسم العميل") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text("رقم الهاتف") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customerBalance,
                            onValueChange = { customerBalance = it },
                            label = { Text("الرصيد / الدين") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val balanceVal = customerBalance.toDoubleOrNull() ?: 0.0
                                if (customerName.isNotBlank()) {
                                    viewModel.addCustomer(customerName, customerPhone, balanceVal)
                                    customerName = ""
                                    customerPhone = ""
                                    customerBalance = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("إضافة عميل")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(customers) { customer ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = customer.name, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "الهاتف: ${customer.phone}")
                                            Text(text = "المبلغ: ${customer.balance}")
                                        }
                                        Button(onClick = { viewModel.deleteCustomer(customer) }) {
                                            Text("حذف")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            label = { Text("اسم المنتج") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = productCategory,
                            onValueChange = { productCategory = it },
                            label = { Text("القسم") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = productQuantity,
                            onValueChange = { productQuantity = it },
                            label = { Text("الكمية") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = productUnit,
                            onValueChange = { productUnit = it },
                            label = { Text("الوحدة (حبة، كيس، متر...)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = productPrice,
                            onValueChange = { productPrice = it },
                            label = { Text("السعر") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val qtyVal = productQuantity.toDoubleOrNull() ?: 0.0
                                val priceVal = productPrice.toDoubleOrNull() ?: 0.0
                                if (productName.isNotBlank()) {
                                    viewModel.addProduct(productName, productCategory, qtyVal, productUnit, priceVal)
                                    productName = ""
                                    productCategory = ""
                                    productQuantity = ""
                                    productUnit = ""
                                    productPrice = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("إضافة للمخزن")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(products) { product ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                                            Text(text = "القسم: ${product.category}")
                                            Text(text = "الكمية: ${product.quantity} ${product.unit}")
                                            Text(text = "السعر: ${product.price}")
                                        }
                                        Button(onClick = { viewModel.deleteProduct(product) }) {
                                            Text("حذف")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        OutlinedTextField(
                            value = shopName,
                            onValueChange = { shopName = it },
                            label = { Text("اسم المحل / المتجر") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = ownerName,
                            onValueChange = { ownerName = it },
                            label = { Text("اسم المالك") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("رقم الهاتف") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = whatsapp,
                            onValueChange = { whatsapp = it },
                            label = { Text("رقم واتساب") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("العنوان") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reminderMessageTemplate,
                            onValueChange = { reminderMessageTemplate = it },
                            label = { Text("قالب رسالة التذكير") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.saveSettings(shopName, ownerName, phone, whatsapp, address, reminderMessageTemplate)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("حفظ الإعدادات")
                        }
                    }
                }
            }
        }
    }
}
