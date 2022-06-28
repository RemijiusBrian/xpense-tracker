package com.xpenses.android.feature_bills.presentation.bills_list

import com.xpenses.android.feature_bills.domain.model.BillPayment

interface BillsListActions {
    fun onMarkAsPaidClick(payment: BillPayment)
    fun onBillClick(id: Long)
}