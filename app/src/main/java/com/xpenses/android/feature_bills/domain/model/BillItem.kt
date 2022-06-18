package com.xpenses.android.feature_bills.domain.model

data class BillItem(
    val id: Long,
    val name: String,
    val category: BillCategory,
    val amount: String
)