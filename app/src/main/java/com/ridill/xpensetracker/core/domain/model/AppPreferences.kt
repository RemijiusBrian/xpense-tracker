package com.ridill.xpensetracker.core.domain.model

data class AppPreferences(
    val expenditureLimit: Long,
    val cashFlowIncludedInExpenditure: Boolean,
)