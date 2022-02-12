package com.ridill.xpensetracker.feature_cash_flow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent

@Entity
data class CashFlowAgentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,

) {
    fun toAgent(): CashFlowAgent = CashFlowAgent(
        id = id,
        name = name,
    )
}