package dev.ridill.xpensetracker.feature_bills.presentation.bills_list

import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory
import dev.ridill.xpensetracker.feature_bills.domain.model.BillItem
import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment
import dev.ridill.xpensetracker.feature_bills.domain.model.BillState

data class BillsListState(
    val billsList: Map<BillCategory, List<BillItem>> = emptyMap(),
    val billPayments: Map<BillState, List<BillPayment>> = emptyMap()
) {
    companion object {
        val INITIAL = BillsListState()
    }
}