package dev.ridill.xpensetracker.feature_bills.data.repository

import dev.ridill.xpensetracker.core.util.DispatcherProvider
import dev.ridill.xpensetracker.feature_bills.data.local.BillsDao
import dev.ridill.xpensetracker.feature_bills.data.mapper.toBill
import dev.ridill.xpensetracker.feature_bills.data.mapper.toBillItem
import dev.ridill.xpensetracker.feature_bills.data.mapper.toBillPayment
import dev.ridill.xpensetracker.feature_bills.data.mapper.toEntity
import dev.ridill.xpensetracker.feature_bills.domain.model.*
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BillsRepositoryImpl(
    private val dao: BillsDao,
    private val dispatcherProvider: DispatcherProvider,
    private val expenseRepo: ExpenseRepository
) : BillsRepository {

    override fun getBillsGroupedByCategory(): Flow<Map<BillCategory, List<BillItem>>> =
        dao.getAllBills().map { entities ->
            entities.map { it.toBillItem() }
        }.map { bills ->
            bills.groupBy { it.category }
        }

    override fun getBillPaymentsForCurrentMonth(): Flow<List<BillPayment>> =
        dao.getBillsWithExpensesForCurrentMonthOrRecurring().map { billsWithExpenses ->
            billsWithExpenses.map { it.toBillPayment() }
        }

    override suspend fun getBillById(id: Long): Bill? = withContext(dispatcherProvider.io) {
        dao.getBillById(id)?.toBill()
    }

    override suspend fun markBillAsPaid(payment: BillPayment) {
        if (payment.state == BillState.PAID) return
        withContext(dispatcherProvider.io) {
            val expense = Expense(
                name = payment.name,
                amount = payment.amount.toString(),
                dateMillis = System.currentTimeMillis(),
                billId = payment.id,
                tag = null
            )
            expenseRepo.cacheExpense(expense)
        }
    }

    override suspend fun cacheBill(bill: Bill) = withContext(dispatcherProvider.io) {
        dao.insert(bill.toEntity())
    }

    override suspend fun deleteBill(id: Long) = withContext(dispatcherProvider.io) {
        dao.deleteById(id)
    }
}