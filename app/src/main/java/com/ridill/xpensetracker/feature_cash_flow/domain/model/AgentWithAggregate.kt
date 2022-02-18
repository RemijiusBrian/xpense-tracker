package com.ridill.xpensetracker.feature_cash_flow.domain.model

import com.ridill.xpensetracker.core.ui.util.TextUtil

data class AgentWithAggregate(
    val id: Long,
    val name: String,
    val createdDateMillis: Long,
    val aggregate: Long
) {
    val createdDateFormatted: String
        get() = TextUtil.formatDate(createdDateMillis)

    val isPending: Boolean get() = aggregate != 0L
}