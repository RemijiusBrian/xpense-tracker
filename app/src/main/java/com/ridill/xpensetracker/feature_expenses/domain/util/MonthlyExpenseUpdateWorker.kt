package com.ridill.xpensetracker.feature_expenses.domain.util

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ridill.xpensetracker.feature_expenses.domain.use_case.CheckCurrentMonthAndUpdateMonthlyExpenseUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class MonthlyExpenseUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val checkCurrentMonthAndUpdateMonthlyExpenseUseCase: CheckCurrentMonthAndUpdateMonthlyExpenseUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            checkCurrentMonthAndUpdateMonthlyExpenseUseCase()
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}
