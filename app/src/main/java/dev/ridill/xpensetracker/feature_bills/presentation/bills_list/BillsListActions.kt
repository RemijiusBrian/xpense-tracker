package dev.ridill.xpensetracker.feature_bills.presentation.bills_list

import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment

interface BillsListActions {
    fun onMarkAsPaidClick(payment: BillPayment)
    fun onBillClick(id: Long)
}