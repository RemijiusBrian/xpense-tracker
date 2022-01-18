package com.ridill.xpensetracker.feature_cash_flow.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowDao {

    @Query("SELECT * FROM CashFlowEntity WHERE expenseId = :expenseId")
    fun getCashFlowForExpense(expenseId: Long): Flow<List<CashFlowEntity>>

    @Query("SELECT * FROM CashFlowEntity WHERE expenseId = :expenseId AND name = :name")
    suspend fun getCashFlowById(expenseId: Long, name: String): CashFlowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cashFlowEntity: CashFlowEntity): Long

    @Delete
    suspend fun delete(cashFlowEntity: CashFlowEntity): Int

    @Query("DELETE FROM CashFlowEntity")
    suspend fun deleteAll()
}