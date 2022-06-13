package com.xpenses.android.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xpenses.android.feature_expenses.data.local.ExpenseDao
import com.xpenses.android.feature_expenses.data.local.ExpenseTagDao
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseTagEntity

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
    abstract val expenseTagDao: ExpenseTagDao
}