package com.smart.assistant.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.assistant.data.local.dao.CustomerDao
import com.smart.assistant.data.local.dao.ProductDao
import com.smart.assistant.data.local.dao.ShopSettingsDao
import com.smart.assistant.data.local.entity.CustomerEntity
import com.smart.assistant.data.local.entity.ProductEntity
import com.smart.assistant.data.local.entity.ShopSettingsEntity

@Database(
    entities = [CustomerEntity::class, ProductEntity::class, ShopSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun shopSettingsDao(): ShopSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_assistant_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
