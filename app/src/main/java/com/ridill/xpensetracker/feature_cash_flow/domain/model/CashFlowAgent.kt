package com.ridill.xpensetracker.feature_cash_flow.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashFlowAgent(
    val id: Long = 0L,
    val name: String,
    val aggregateAmount: Long = 0L
) : Parcelable {
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