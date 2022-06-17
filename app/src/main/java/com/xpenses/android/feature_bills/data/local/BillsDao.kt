package com.xpenses.android.feature_bills.data.local

import androidx.room.Dao
import androidx.room.Query
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillsDao {

    @Query("SELECT * FROM ExpenseEntity WHERE billId = :billId")
    fun getPaymentsForBill(billId: Long): Flow<List<ExpenseEntity>>
}