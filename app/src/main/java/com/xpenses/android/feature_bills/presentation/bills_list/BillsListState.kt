package com.xpenses.android.feature_bills.presentation.bills_list

import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment

data class BillsListState(
    val billsList: List<BillItem> = emptyList(),
    val billPayments: List<BillPayment> = emptyList()
) {
    companion object {
        val INITIAL = BillsListState()
    }
}