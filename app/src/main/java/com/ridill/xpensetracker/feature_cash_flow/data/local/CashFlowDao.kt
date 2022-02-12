package com.ridill.xpensetracker.feature_cash_flow.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowDao {

    @Query("SELECT * FROM CashFlowAgentEntity WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun getAgents(query: String): Flow<List<CashFlowAgentEntity>>

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

    @Query("SELECT * FROM CashFlowAgentEntity WHERE id = :id")
    suspend fun getAgentById(id: Long): CashFlowAgentEntity?

    @Query("SELECT * FROM CashFlowAgentEntity WHERE name = :name")
    suspend fun getAgentByName(name: String): CashFlowAgentEntity?

    @Query("SELECT * FROM CashFlowEntity WHERE id = :id")
    suspend fun getCashFlowById(id: Long): CashFlowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agentEntity: CashFlowAgentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashFlow(cashFlowEntity: CashFlowEntity): Long

    @Delete
    suspend fun deleteAgent(agentEntity: CashFlowAgentEntity): Int

    @Delete
    suspend fun deleteCashFlow(cashFlowEntity: CashFlowEntity): Int

    @Transaction
    suspend fun clearAgent(agentEntity: CashFlowAgentEntity) {
        deleteAllCashFlowForAgent(agentEntity.id)
        deleteAgent(agentEntity)
    }

    @Query("DELETE FROM CashFlowEntity WHERE agent = :agent")
    suspend fun deleteAllCashFlowForAgent(agent: Long)
}