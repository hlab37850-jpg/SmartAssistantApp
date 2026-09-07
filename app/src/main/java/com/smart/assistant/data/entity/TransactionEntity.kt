package com.smart.assistant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val type: String,
    val amount: Double,
    val notes: String,
    val date: Long = System.currentTimeMillis()
)
