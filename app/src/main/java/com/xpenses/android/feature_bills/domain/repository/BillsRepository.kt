package com.xpenses.android.feature_bills.domain.repository

import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment
import kotlinx.coroutines.flow.Flow

interface BillsRepository {

    fun getBills(): Flow<List<BillItem>>

    fun getBillPaymentsForCurrentMonth(): Flow<List<BillPayment>>

    suspend fun markBillAsPaid(payment: BillPayment)
}