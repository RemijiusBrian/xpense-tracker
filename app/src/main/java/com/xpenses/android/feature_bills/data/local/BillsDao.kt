package com.xpenses.android.feature_bills.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.xpenses.android.feature_bills.data.local.entity.BillEntity
import com.xpenses.android.feature_bills.data.local.relation.BillWithExpensesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface BillsDao {

    @Query("SELECT * FROM BillEntity ORDER BY category ASC")
    fun getAllBills(): Flow<List<BillEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM BillEntity
        WHERE (strftime('%m', payByDate / 1000, 'unixepoch') = strftime('%m', date('now'))) OR recurring = 1
        ORDER BY payByDate DESC
        """
    )
    fun getBillsWithExpensesForCurrentMonth(): Flow<List<BillWithExpensesRelation>>
}