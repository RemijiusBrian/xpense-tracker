package com.ridill.xpensetracker.feature_expenditures.data.repository

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.data.local.ExpenditureDao
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenditureRepositoryImpl(
    private val dao: ExpenditureDao
) : ExpenditureRepository {

    override fun getExpenditures(category: ExpenditureCategory): Flow<List<Expenditure>> =
        dao.getExpenses(category).map { entities -> entities.map { it.toExpenditure() } }

    override fun getTotalExpenditureAmountForCurrentMonth(): Flow<Long> =
        dao.getExpenditureForCurrentMonth(ExpenditureCategory.YEARNING).map { it ?: 0L }

    override suspend fun getExpenditureById(id: Long): Expenditure? =
        dao.getExpenditureById(id)?.toExpenditure()

    override suspend fun getExpenditureByName(name: String): Expenditure? =
        dao.getExpenditureByName(name)?.toExpenditure()

    override suspend fun cacheExpenditure(expenditure: Expenditure): Long =
        dao.insert(expenditure.toEntity())

    override suspend fun deleteExpenditure(expenditure: Expenditure) = withContext(Dispatchers.IO) {
        dao.delete(expenditure.toEntity())
    }
}