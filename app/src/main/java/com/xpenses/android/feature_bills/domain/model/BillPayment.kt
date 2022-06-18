package com.xpenses.android.feature_bills.domain.model

data class BillPayment(
    val id: Long,
    val paymentOrPayByDate: String,
    val amount: String,
    val category: BillCategory,
    val name: String,
    val state: BillState
)