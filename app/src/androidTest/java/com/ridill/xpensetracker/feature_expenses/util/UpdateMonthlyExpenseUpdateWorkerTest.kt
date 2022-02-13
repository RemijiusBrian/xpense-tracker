package com.ridill.xpensetracker.feature_expenses.util

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.common.truth.Truth.assertThat
import com.ridill.xpensetracker.feature_expenses.domain.util.MonthlyExpenseUpdateWorker
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class UpdateMonthlyExpenseUpdateWorkerTest {

    private lateinit var context: Context
    private lateinit var configuration: Configuration
    private lateinit var workManager: WorkManager


    @Inject
    lateinit var worker: MonthlyExpenseUpdateWorker

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        configuration = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
        workManager = WorkManager.getInstance(context)

        hiltRule.inject()
    }

    @Test
    fun testMonthlyExpenseUpdateWorker_returnsSuccess() = runBlocking {

        val result = worker.doWork()
//        val workInfo = workManager.getWorkInfoById(workRequest.id).get()
        assertThat(result).isSameInstanceAs(ListenableWorker.Result.success())
    }
}