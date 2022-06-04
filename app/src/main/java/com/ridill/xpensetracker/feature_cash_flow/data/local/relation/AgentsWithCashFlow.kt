package com.ridill.xpensetracker.feature_cash_flow.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate

data class AgentsWithCashFlow(
    @Embedded val agentEntity: CashFlowAgentEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "agent"
    )
    val cashFlowEntities: List<CashFlowEntity>
) {

    fun toAgentWithAggregate(): AgentWithAggregate = AgentWithAggregate(
        name = agentEntity.name,
        isPending = cashFlowEntities.sumOf { it.amount } > 0,
        createdDateMillis = agentEntity.createdDateMillis,
        id = agentEntity.id
    )
}