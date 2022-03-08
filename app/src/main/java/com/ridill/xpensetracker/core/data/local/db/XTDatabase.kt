package com.ridill.xpensetracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity

@Database(
    entities = [
        ExpenseEntity::class,
        ExpenseTagEntity::class,
        CashFlowAgentEntity::class,
        CashFlowEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class XTDatabase : RoomDatabase() {

    companion object {
        const val NAME = "XpenseTracker.db"
    }

    // Dao
    abstract val expenseDao: ExpenseDao
    abstract val cashFlowDao: CashFlowDao
}