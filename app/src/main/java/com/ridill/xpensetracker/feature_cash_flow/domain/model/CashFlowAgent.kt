package com.ridill.xpensetracker.feature_cash_flow.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowAgentEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashFlowAgent(
    val id: Long = 0L,
    val name: String,
    val createdDateMillis: Long,
) : Parcelable {
    val createdDateFormatted: String get() = TextUtil.formatDate(createdDateMillis)

    fun toEntity(): CashFlowAgentEntity = CashFlowAgentEntity(
        id = id,
        name = name,
        createdDateMillis = createdDateMillis
    )

    companion object {
        val DEFAULT = CashFlowAgent(
            name = "",
            createdDateMillis = System.currentTimeMillis()
        )
    }
}