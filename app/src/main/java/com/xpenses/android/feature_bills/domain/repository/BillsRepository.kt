package com.xpenses.android.feature_bills.domain.repository

import com.xpenses.android.feature_bills.domain.model.BillItem
import kotlinx.coroutines.flow.Flow

interface BillsRepository {

    fun getBills(): Flow<List<BillItem>>

    fun getBillsWithExpenses(): Flow<List<BillItem>>
}