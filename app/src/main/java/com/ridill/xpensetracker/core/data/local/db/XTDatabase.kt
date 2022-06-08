package com.ridill.xpensetracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity

@Database(
    entities = [
        ExpenseEntity::class,
        ExpenseTagEntity::class,
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
}