package com.xpenses.android.feature_bills.data.repository

import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_bills.data.local.BillsDao
import com.xpenses.android.feature_bills.data.mapper.toBillItem
import com.xpenses.android.feature_bills.data.mapper.toBillWithPayments
import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillsRepositoryImpl(
    private val dao: BillsDao,
    private val dispatcherProvider: DispatcherProvider
) : BillsRepository {

    override fun getBills(): Flow<List<BillItem>> = dao.getAllBills().map { entities ->
        entities.map { it.toBillItem() }
    }

    override fun getBillsWithExpenses(): Flow<List<BillItem>> =
        dao.getBillsWithExpensesForCurrentMonth().map { relation ->
            relation.map { it.toBillWithPayments() }
        }
}