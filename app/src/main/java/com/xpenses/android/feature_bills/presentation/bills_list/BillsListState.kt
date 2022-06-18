package com.xpenses.android.feature_bills.presentation.bills_list

import com.xpenses.android.feature_bills.domain.model.BillItem

data class BillsListState(
    val billsList: List<BillItem> = emptyList(),
    val upcomingPayments: List<BillItem> = emptyList()
) {
    companion object {
        val INITIAL = BillsListState()
    }
}