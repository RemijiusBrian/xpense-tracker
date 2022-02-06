package com.ridill.xpensetracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ridill.xpensetracker.feature_cash_flow.data.local.CashFlowDao
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import com.ridill.xpensetracker.feature_expenditures.data.local.ExpenditureDao
import com.ridill.xpensetracker.feature_expenditures.data.local.entity.ExpenditureEntity

@Database(
    entities = [
        ExpenditureEntity::class,
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
    abstract val expenditureDao: ExpenditureDao
    abstract val cashFlowDao: CashFlowDao
}