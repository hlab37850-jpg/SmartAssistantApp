package com.smart.assistant.domain.model

data class DashboardStats(
    val totalCustomers: Int = 0,
    val totalProducts: Int = 0,
    val totalPositiveBalance: Double = 0.0,
    val totalNegativeBalance: Double = 0.0
)
