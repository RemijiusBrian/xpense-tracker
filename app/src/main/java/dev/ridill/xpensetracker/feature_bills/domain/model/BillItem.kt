package dev.ridill.xpensetracker.feature_bills.domain.model

data class BillItem(
    val id: Long,
    val name: String,
    val category: BillCategory,
    val amount: String,
    val payBy: String
)