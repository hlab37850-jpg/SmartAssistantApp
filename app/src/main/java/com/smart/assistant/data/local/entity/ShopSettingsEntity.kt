package com.smart.assistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_settings")
data class ShopSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val shopName: String = "",
    val phone: String = "",
    val whatsapp: String = "",
    val reminderMessageTemplate: String = ""
)
