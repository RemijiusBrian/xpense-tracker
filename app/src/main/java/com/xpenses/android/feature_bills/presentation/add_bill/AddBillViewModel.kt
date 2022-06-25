package com.xpenses.android.feature_bills.presentation.add_bill

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.xpenses.android.R
import com.xpenses.android.core.domain.model.UiText
import com.xpenses.android.core.util.toDoubleOrZero
import com.xpenses.android.feature_bills.domain.model.Bill
import com.xpenses.android.feature_bills.domain.model.BillCategory
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBillViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: BillsRepository
) : ViewModel(), AddBillActions {

    private val bill = savedStateHandle.getLiveData<Bill>(KEY_BILL_LIVE_DATA, Bill.DEFAULT)
    val name = bill.map { it.name }
    val amount = bill.map { it.amount }
    val category = bill.map { it.category }
    val payByDate = bill.map { it.dateFormatted }

    private val eventsChannel = Channel<AddBillEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    override fun onNameChange(value: String) {
        bill.value = bill.value?.copy(
            name = value
        )
    }

    override fun onAmountChange(value: String) {
        bill.value = bill.value?.copy(
            amount = value
        )
    }

    override fun onMarkAsRecurringCheckChange(isChecked: Boolean) {
        bill.value = bill.value?.copy(
            recurring = isChecked
        )
    }

    override fun onCategoryClick() {
    }

    override fun onCategorySelect(category: BillCategory) {
        bill.value = bill.value?.copy(
            category = category
        )
    }

    override fun onPayByDateChange(dateMillis: Long) {
        bill.value = bill.value?.copy(
            dateMillis = dateMillis
        )
    }

    override fun onSave() {
        viewModelScope.launch {
            val amount = amount.value?.trim().orEmpty()
            if (amount.toDoubleOrZero() <= 0.0) {
                eventsChannel.send(AddBillEvent.ShowSnackbar(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            val name = name.value?.trim().orEmpty()
            if (name.isEmpty()) {
                eventsChannel.send(AddBillEvent.ShowSnackbar(UiText.StringResource(R.string.error_invalid_name)))
                return@launch
            }
            bill.value?.let { repo.cacheBill(it) }
            eventsChannel.send(AddBillEvent.BillAdded)
        }
    }

    sealed class AddBillEvent {
        data class ShowSnackbar(val message: UiText) : AddBillEvent()
        object BillAdded : AddBillEvent()
    }
}

private const val KEY_BILL_LIVE_DATA = "KEY_BILL_LIVE_DATA"

const val ADD_BILL_RESULT = "ADD_BILL_RESULT"
const val RESULT_BILL_ADDED = "RESULT_BILL_ADDED"
