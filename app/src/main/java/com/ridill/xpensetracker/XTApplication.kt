package com.ridill.xpensetracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.util.MonthlyExpenseUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class XTApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var preferencesManager: XTPreferencesManager

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        submitMonthlyExpenseWorkRequest()
    }

    private fun submitMonthlyExpenseWorkRequest() = CoroutineScope(SupervisorJob()).launch {
        if (preferencesManager.preferences.first().isFirstAppLaunch) {
            val updateMonthlyExpenseWorkRequest =
                PeriodicWorkRequestBuilder<MonthlyExpenseUpdateWorker>(1, TimeUnit.DAYS)
                    .build()

            WorkManager.getInstance(this@XTApplication)
                .enqueueUniquePeriodicWork(
                    MONTHLY_EXPENSE_UPDATE,
                    ExistingPeriodicWorkPolicy.KEEP,
                    updateMonthlyExpenseWorkRequest
                )

            preferencesManager.toggleIsFirstLaunchFalse()
        }
    }
}

private const val MONTHLY_EXPENSE_UPDATE = "MONTHLY_EXPENSE_UPDATE"