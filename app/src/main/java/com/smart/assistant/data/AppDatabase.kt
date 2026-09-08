package com.smart.assistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.assistant.data.dao.CustomerDao
import com.smart.assistant.data.dao.ProductDao
import com.smart.assistant.data.dao.SettingsDao
import com.smart.assistant.data.entity.CustomerEntity
import com.smart.assistant.data.entity.ProductEntity
import com.smart.assistant.data.entity.SettingsEntity

@Database(entities = [CustomerEntity::class, ProductEntity::class, SettingsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_assistant_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
