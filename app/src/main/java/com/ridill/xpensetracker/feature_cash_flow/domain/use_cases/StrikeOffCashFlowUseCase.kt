package com.ridill.xpensetracker.feature_cash_flow.domain.use_cases

import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository

class StrikeOffCashFlowUseCase(
    private val cashFlowRepository: CashFlowRepository,
    private val expenditureRepository: ExpenditureRepository
) {
    suspend operator fun invoke(expenditure: Expenditure) {
        cashFlowRepository.clearCache()
        expenditureRepository.deleteExpenditure(expenditure)
    }
}