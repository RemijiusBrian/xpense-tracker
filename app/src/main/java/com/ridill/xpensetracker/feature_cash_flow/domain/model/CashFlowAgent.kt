package com.ridill.xpensetracker.feature_cash_flow.domain.model

import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity

data class CashFlowAgent(
    val id: Long = 0L,
    val name: String,
    val aggregateAmount: Long = 0L
) {
    val isPending get() = aggregateAmount != 0L

    fun toEntity(): CashFlowAgentEntity = CashFlowAgentEntity(
        id = id,
        name = name,
    )

    companion object {
        val DEFAULT = CashFlowAgent(
            name = "",
        )
    }
}