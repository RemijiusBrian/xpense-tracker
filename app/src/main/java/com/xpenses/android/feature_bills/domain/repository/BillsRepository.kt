package com.xpenses.android.feature_bills.domain.repository

import com.xpenses.android.feature_bills.domain.model.Bill
import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment
import kotlinx.coroutines.flow.Flow

interface BillsRepository {

    suspend fun getAllBillsList(): List<Bill>

    fun getBills(): Flow<List<BillItem>>

    fun getBillPaymentsForCurrentMonth(): Flow<List<BillPayment>>

    suspend fun markBillAsPaid(payment: BillPayment)

    suspend fun cacheBill(bill: Bill)
}