package com.xpenses.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.xpenses.android.feature_bills.domain.bill_reminder.DailyBillCheckManager
import dagger.hilt.android.HiltAndroidApp
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