package dev.ridill.xpensetracker.feature_bills.domain.repository

import dev.ridill.xpensetracker.feature_bills.domain.model.*
import kotlinx.coroutines.flow.Flow

interface BillsRepository {

    fun getBillsGroupedByCategory(): Flow<Map<BillCategory, List<BillItem>>>

    fun getBillPaymentsForCurrentMonth(): Flow<Map<BillState, List<BillPayment>>>

    suspend fun getBillById(id: Long): Bill?

    suspend fun markBillAsPaid(payment: BillPayment)

    suspend fun cacheBill(bill: Bill)

    suspend fun deleteBill(id: Long)
}