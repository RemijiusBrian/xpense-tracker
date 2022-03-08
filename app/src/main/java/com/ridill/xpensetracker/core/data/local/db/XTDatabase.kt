package com.ridill.xpensetracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
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

// Database Migration
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE ExpenseTagEntity(
            name TEXT PRIMARY KEY
            )
        """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE ExpenseEntity
            ADD COLUMN tag TEXT REFERENCES ExpenseTagEntity(name)
        """.trimIndent()
        )
    }
}