package com.ridill.xpensetracker.feature_expenditures.domain.repository

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
import kotlinx.coroutines.flow.Flow

interface ExpenditureRepository {

    fun getExpenditures(category: ExpenditureCategory): Flow<List<Expenditure>>

    fun getTotalExpenditureAmountForCurrentMonth(): Flow<Long>

    suspend fun getExpenditureById(id: Long): Expenditure?

    suspend fun getExpenditureByName(name: String): Expenditure?

    suspend fun cacheExpenditure(expenditure: Expenditure): Long

    suspend fun deleteExpenditure(expenditure: Expenditure)
}