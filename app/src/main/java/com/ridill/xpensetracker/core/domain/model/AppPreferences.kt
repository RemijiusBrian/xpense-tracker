package com.ridill.xpensetracker.core.domain.model

data class AppPreferences(
    val theme: AppTheme,
    val expenditureLimit: Long,
    val cashFlowIncludedInExpenditure: Boolean,
    val isFirstAppLaunch: Boolean
)