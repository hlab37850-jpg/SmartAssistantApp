package com.smart.assistant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val storeName: String,
    val ownerName: String,
    val phone: String,
    val address: String
)
