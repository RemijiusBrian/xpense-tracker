package com.xpenses.android.feature_bills.data.repository

import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_bills.data.local.BillsDao
import com.xpenses.android.feature_bills.data.mapper.toBillItem
import com.xpenses.android.feature_bills.data.mapper.toBillPayment
import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import com.xpenses.android.feature_expenses.domain.model.Expense
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BillsRepositoryImpl(
    private val dao: BillsDao,
    private val dispatcherProvider: DispatcherProvider,
    private val expenseRepo: ExpenseRepository
) : BillsRepository {

    override fun getBills(): Flow<List<BillItem>> = dao.getAllBills().map { entities ->
        entities.map { it.toBillItem() }
    }

    override fun getBillPaymentsForCurrentMonth(): Flow<List<BillPayment>> =
        dao.getBillsWithExpensesForCurrentMonth().map { billsWithExpenses ->
            billsWithExpenses.map { it.toBillPayment() }
        }

    override suspend fun markBillAsPaid(payment: BillPayment) {
        withContext(dispatcherProvider.io) {
            val expense = Expense(
                name = payment.name,
                amount = payment.amount,
                dateMillis = System.currentTimeMillis(),
                billId = payment.id,
                tag = null
            )
            expenseRepo.cacheExpense(expense)
        }
    }
}