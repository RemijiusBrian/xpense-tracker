package com.ridill.xpensetracker.feature_cash_flow.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowDao {

    @Query("SELECT * FROM CashFlowAgentEntity WHERE name LIKE '%' || :query || '%' ORDER BY createdDateMillis DESC")
    fun getAgents(query: String): Flow<List<CashFlowAgentEntity>>

    /*@Query(
        """
        SELECT ca.id as id, ca.name as name, (
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 1 AND agent = ca.id), 0) - 
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 0 AND agent = ca.id), 0)
        ) as aggregate
        FROM CashFlowEntity cf
        LEFT JOIN CashFlowAgentEntity ca ON cf.agent = ca.id
        GROUP BY ca.id
        HAVING ca.name LIKE '%' || :query || '%' ORDER BY ca.name
    """
    )
    fun getAgentsWithAggregate(query: String): Flow<List<AgentWithAggregate>>*/

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