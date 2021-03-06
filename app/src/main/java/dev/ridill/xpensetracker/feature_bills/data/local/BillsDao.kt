package dev.ridill.xpensetracker.feature_bills.data.local

import androidx.room.*
import dev.ridill.xpensetracker.feature_bills.data.local.entity.BillEntity
import dev.ridill.xpensetracker.feature_bills.data.local.relation.BillWithExpensesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface BillsDao {

    @Query("SELECT * FROM BillEntity")
    suspend fun getAllBillsList(): List<BillEntity>

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
    fun getBillsWithExpensesForCurrentMonthOrRecurring(): Flow<List<BillWithExpensesRelation>>

    @Query("SELECT * FROM BillEntity WHERE id = :id")
    suspend fun getBillById(id: Long): BillEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(billEntity: BillEntity)

    @Query("DELETE FROM BillEntity WHERE id = :id")
    suspend fun deleteById(id: Long)
}