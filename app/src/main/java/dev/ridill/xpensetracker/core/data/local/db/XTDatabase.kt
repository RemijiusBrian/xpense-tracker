package dev.ridill.xpensetracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ridill.xpensetracker.feature_bills.data.local.BillsDao
import dev.ridill.xpensetracker.feature_bills.data.local.entity.BillEntity
import dev.ridill.xpensetracker.feature_expenses.data.local.ExpenseDao
import dev.ridill.xpensetracker.feature_expenses.data.local.ExpenseTagDao
import dev.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import dev.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity

@Database(
    entities = [
        ExpenseEntity::class,
        ExpenseTagEntity::class,
        BillEntity::class
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
    abstract val billsDao: BillsDao
}