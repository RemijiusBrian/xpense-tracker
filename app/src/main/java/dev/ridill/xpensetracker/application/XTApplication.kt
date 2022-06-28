package dev.ridill.xpensetracker.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import dev.ridill.xpensetracker.feature_bills.domain.bill_reminder.DailyBillCheckManager
import javax.inject.Inject

@HiltAndroidApp
class XTApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var dailyBillCheckManager: DailyBillCheckManager

    override fun onCreate() {
        super.onCreate()
        dailyBillCheckManager.scheduleDailyBillCheckManager()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}