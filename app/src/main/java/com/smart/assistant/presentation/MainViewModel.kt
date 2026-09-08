package com.smart.assistant.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smart.assistant.data.AppDatabase
import com.smart.assistant.data.entity.CustomerEntity
import com.smart.assistant.data.entity.ProductEntity
import com.smart.assistant.data.entity.SettingsEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val customerDao = database.customerDao()
    private val productDao = database.productDao()
    private val settingsDao = database.settingsDao()

    val customers: StateFlow<List<CustomerEntity>> = customerDao.getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val products: StateFlow<List<ProductEntity>> = productDao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val settings: StateFlow<SettingsEntity?> = settingsDao.getSettings()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun addCustomer(name: String, phone: String, balance: Double) {
        viewModelScope.launch {
            customerDao.insertCustomer(CustomerEntity(name = name, phone = phone, balance = balance))
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            customerDao.deleteCustomer(customer)
        }
    }

    fun addProduct(name: String, category: String, quantity: Double, unit: String, price: Double) {
        viewModelScope.launch {
            productDao.insertProduct(ProductEntity(name = name, category = category, quantity = quantity, unit = unit, price = price))
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }

    fun saveSettings(shopName: String, ownerName: String, phone: String, whatsapp: String, address: String, reminderMessageTemplate: String) {
        viewModelScope.launch {
            val current = settings.value ?: SettingsEntity()
            settingsDao.insertOrUpdateSettings(
                current.copy(
                    shopName = shopName,
                    ownerName = ownerName,
                    phone = phone,
                    whatsapp = whatsapp,
                    address = address,
                    reminderMessageTemplate = reminderMessageTemplate
                )
            )
        }
    }
}
