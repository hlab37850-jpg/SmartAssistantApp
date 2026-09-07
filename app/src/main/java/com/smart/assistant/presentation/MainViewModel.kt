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
    private val db = AppDatabase.getDatabase(application)

    val customers: StateFlow<List<CustomerEntity>> = db.customerDao().getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<ProductEntity>> = db.productDao().getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SettingsEntity?> = db.settingsDao().getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addCustomer(name: String, phone: String, balance: Double) {
        viewModelScope.launch {
            db.customerDao().insertCustomer(CustomerEntity(name = name, phone = phone, balance = balance))
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            db.customerDao().deleteCustomer(customer)
        }
    }

    fun addProduct(name: String, category: String, quantity: Double, unit: String, price: Double) {
        viewModelScope.launch {
            db.productDao().insertProduct(ProductEntity(name = name, category = category, quantity = quantity, unit = unit, price = price))
        }
    }

    fun updateProductQuantity(productId: Long, newQuantity: Double) {
        viewModelScope.launch {
            db.productDao().updateQuantity(productId, newQuantity)
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            db.productDao().deleteProduct(product)
        }
    }

    fun saveSettings(storeName: String, ownerName: String, phone: String, address: String) {
        viewModelScope.launch {
            db.settingsDao().saveSettings(SettingsEntity(id = 1, storeName = storeName, ownerName = ownerName, phone = phone, address = address))
        }
    }
}
