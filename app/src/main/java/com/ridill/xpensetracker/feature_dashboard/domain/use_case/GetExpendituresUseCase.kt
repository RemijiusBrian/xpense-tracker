package com.ridill.xpensetracker.feature_dashboard.domain.use_case

import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
import com.ridill.xpensetracker.feature_expenditures.domain.repository.ExpenditureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class GetExpendituresUseCase(
    private val repository: ExpenditureRepository
) {
    operator fun invoke(category: ExpenditureCategory): Flow<Map<String, List<Expenditure>>> =
        repository.getExpenditures(category).map { expenses ->
            expenses.groupBy {
                SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(it.dateMillis)
            }
        }
}
