package com.smart.assistant.data.dao

import androidx.room.*
import com.smart.assistant.data.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY id DESC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)
}
