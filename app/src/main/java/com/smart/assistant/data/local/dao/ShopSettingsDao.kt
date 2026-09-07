package com.smart.assistant.data.local.dao

import androidx.room.*
import com.smart.assistant.data.local.entity.ShopSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopSettingsDao {
    @Query("SELECT * FROM shop_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<ShopSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: ShopSettingsEntity)
}
