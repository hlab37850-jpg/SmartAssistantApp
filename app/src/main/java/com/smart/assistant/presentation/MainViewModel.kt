package com.smart.assistant.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smart.assistant.data.local.database.AppDatabase
import com.smart.assistant.data.local.entity.CustomerEntity
import com.smart.assistant.data.local.entity.ProductEntity
import com.smart.assistant.data.local.entity.ShopSettingsEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val customerDao = db.customerDao()
    private val productDao = db.productDao()
    private val settingsDao = db.shopSettingsDao()

    val customers: StateFlow<List<CustomerEntity>> = customerDao.getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<ProductEntity>> = productDao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<ShopSettingsEntity?> = settingsDao.getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addCustomer(name: String, phone: String, balance: Double) {
        viewModelScope.launch {
            customerDao.insertCustomer(CustomerEntity(name = name, phone = phone, balance = balance))
        }
    }

    fun updateCustomerBalance(customer: CustomerEntity, newBalance: Double) {
        viewModelScope.launch {
            customerDao.updateCustomer(customer.copy(balance = newBalance))
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch { customerDao.deleteCustomer(customer) }
    }

    fun addProduct(name: String, category: String, quantity: Int, unit: String) {
        viewModelScope.launch {
            productDao.insertProduct(ProductEntity(name = name, category = category, quantity = quantity, unit = unit))
        }
    }

    fun updateProductQuantity(product: ProductEntity, delta: Int) {
        viewModelScope.launch {
            val newQty = (product.quantity + delta).coerceAtLeast(0)
            productDao.updateProduct(product.copy(quantity = newQty))
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch { productDao.deleteProduct(product) }
    }

    fun saveSettings(shopName: String, phone: String, whatsapp: String, template: String) {
        viewModelScope.launch {
            settingsDao.saveSettings(
                ShopSettingsEntity(
                    id = 1,
                    shopName = shopName,
                    phone = phone,
                    whatsapp = whatsapp,
                    reminderMessageTemplate = template
                )
            )
        }
    }
}
