package com.ridill.xpensetracker.feature_cash_flow.data.local

import androidx.room.*
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import com.ridill.xpensetracker.feature_cash_flow.data.local.relation.AgentsWithCashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowAgentDao {

    @Query(
        """
        SELECT ca.id as id, ca.name as name, ca.createdDateMillis as createdDateMillis, (
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 1 AND agent = ca.id), 0) - 
            IFNULL((SELECT SUM(amount) FROM CashFlowEntity WHERE lent = 0 AND agent = ca.id), 0)
        ) != 0 as isPending
        FROM  CashFlowAgentEntity ca
        LEFT JOIN CashFlowEntity cf ON ca.id = cf.agent
        GROUP BY ca.id
        HAVING ca.name LIKE '%' || :query || '%' ORDER BY ca.name
    """
    )
    fun getAgentsWithAggregate(query: String): Flow<List<AgentWithAggregate>>

    @Transaction
    @Query("SELECT * FROM CashFlowAgentEntity WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getAgentsWithCashFlow(query: String): Flow<List<AgentsWithCashFlow>>

    @Query("SELECT * FROM CashFlowAgentEntity WHERE id = :id")
    suspend fun getAgentById(id: Long): CashFlowAgentEntity?

    @Query("SELECT * FROM CashFlowAgentEntity WHERE name = :name")
    suspend fun getAgentByName(name: String): CashFlowAgentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agentEntity: CashFlowAgentEntity): Long

    @Delete
    suspend fun deleteAgent(agentEntity: CashFlowAgentEntity): Int
}