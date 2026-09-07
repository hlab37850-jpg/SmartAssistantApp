package com.smart.assistant.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.assistant.data.local.dao.CustomerDao
import com.smart.assistant.data.local.dao.ProductDao
import com.smart.assistant.data.local.dao.SettingsDao
import com.smart.assistant.data.local.entity.CustomerEntity
import com.smart.assistant.data.local.entity.ProductEntity
import com.smart.assistant.data.local.entity.SettingsEntity

@Database(
    entities = [CustomerEntity::class, ProductEntity::class, SettingsEntity::class],
    version = 2,
    exportSchema = false
)
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
                    "smart_assistant_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
