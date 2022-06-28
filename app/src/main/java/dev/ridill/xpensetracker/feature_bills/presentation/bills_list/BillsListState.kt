package dev.ridill.xpensetracker.feature_bills.presentation.bills_list

import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory
import dev.ridill.xpensetracker.feature_bills.domain.model.BillItem
import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment

data class BillsListState(
    val billsList: Map<BillCategory, List<BillItem>> = emptyMap(),
    val billPayments: List<BillPayment> = emptyList()
) {
    companion object {
        val INITIAL = BillsListState()
    }
}