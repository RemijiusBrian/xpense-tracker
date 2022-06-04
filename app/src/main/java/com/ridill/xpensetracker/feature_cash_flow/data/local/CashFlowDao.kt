package com.ridill.xpensetracker.feature_cash_flow.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowDao {

    @Query("SELECT * FROM CashFlowEntity WHERE agent = :agent")
    fun getCashFlowForAgent(agent: Long): Flow<List<CashFlowEntity>>

    @Query(
        """
            SELECT
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 1), 0) - 
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 0), 0)
        """
    )
    fun getTotalCashFlowAmount(): Flow<Long>

    @Query(
        """
        SELECT
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE agent = :agent AND lent = 1), 0) - 
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE agent = :agent AND lent = 0), 0)
    """
    )
    fun getAggregateAmountOfAgent(agent: Long): Flow<Long>

    @Query("SELECT * FROM CashFlowEntity WHERE id = :id")
    suspend fun getCashFlowById(id: Long): CashFlowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashFlow(cashFlowEntity: CashFlowEntity): Long

    @Delete
    suspend fun deleteCashFlow(cashFlowEntity: CashFlowEntity): Int

    @Query("DELETE FROM CashFlowEntity WHERE agent = :agent")
    suspend fun deleteAllCashFlowForAgent(agent: Long)
}