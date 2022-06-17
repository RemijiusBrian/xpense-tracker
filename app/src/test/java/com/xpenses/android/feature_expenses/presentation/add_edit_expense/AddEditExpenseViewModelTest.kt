package com.xpenses.android.feature_expenses.presentation.add_edit_expense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.xpenses.android.feature_expenses.data.repository.FakeExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditExpenseViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddEditExpenseViewModel

    @Before
    fun setup() {
        viewModel = AddEditExpenseViewModel(
            SavedStateHandle(),
            FakeExpenseRepository()
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    @Test
    fun addExpenseWithEmptyAmount_returnsError() = runTest {
        viewModel.onAmountChange("")
        viewModel.onNameChange("Name")

        viewModel.onSave()

        val event = viewModel.events.first()

        assertThat(event).isInstanceOf(AddEditExpenseViewModel.AddEditEvents.ShowSnackbar::class.java)
    }

    @Test
    fun addExpenseWithEmptyName_returnsError() = runTest {
        viewModel.onAmountChange("1000")
        viewModel.onNameChange("")

        viewModel.onSave()

        val event = viewModel.events.first()

        assertThat(event).isInstanceOf(AddEditExpenseViewModel.AddEditEvents.ShowSnackbar::class.java)
    }

    @Test
    fun addExpenseWithValidInputs_returnsExpenseCreated() = runTest {
        viewModel.onNameChange("Test")
        viewModel.onAmountChange("1000")

        viewModel.onSave()

        val event = viewModel.events.first()

        assertThat(event).isInstanceOf(AddEditExpenseViewModel.AddEditEvents.ExpenseCreated::class.java)
    }
}