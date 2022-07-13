package dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.AddEditBillScreenSpec
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.core.util.toDoubleOrZero
import dev.ridill.xpensetracker.feature_bills.domain.model.Bill
import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditBillViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: BillsRepository
) : ViewModel(), AddEditBillActions {

    private val billId = AddEditBillScreenSpec.getBillIdFromSavedStateHandle(savedStateHandle)
    val isEditMode = AddEditBillScreenSpec.isEditMode(billId)

    private val bill = savedStateHandle.getLiveData<Bill>(KEY_BILL_LIVE_DATA)
    val name = bill.map { it.name }
    val amount = bill.map { it.amount }
    private val category = bill.map { it.category }.distinctUntilChanged()
    private val payByDate = bill.map { it.dateFormatted }.distinctUntilChanged()
    private val isBillRecurring = bill.map { it.recurring }.distinctUntilChanged()

    private val showCategorySelection =
        savedStateHandle.getLiveData("showCategorySelection", false)

    private val showDeletionConfirmation =
        savedStateHandle.getLiveData("showDeletionConfirmation", false)

    val state = combineTuple(
        showCategorySelection.asFlow(),
        showDeletionConfirmation.asFlow(),
        category.asFlow(),
        payByDate.asFlow(),
        isBillRecurring.asFlow()
    ).map { (
                showCategorySelection,
                showDeletionConfirmation,
                category,
                payByDate,
                isBillRecurring
            ) ->
        AddEditBillState(
            showCategorySelection = showCategorySelection,
            showDeletionConfirmation = showDeletionConfirmation,
            payByDate = payByDate,
            category = category,
            isBillRecurring = isBillRecurring
        )
    }.asLiveData()

    init {
        if (!savedStateHandle.contains(KEY_BILL_LIVE_DATA)) {
            if (billId != null && isEditMode) viewModelScope.launch {
                bill.value = repo.getBillById(billId)
            } else {
                bill.value = Bill.DEFAULT
            }
        }
    }

    private val eventsChannel = Channel<AddEditBillEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    override fun onNameChange(value: String) {
        if (value.length > Constants.BILL_DESCRIPTION_MAX_LENGTH) return
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
        showCategorySelection.value = true
    }

    override fun onCategorySelectionDismiss() {
        showCategorySelection.value = false
    }

    override fun onCategorySelect(category: BillCategory) {
        bill.value = bill.value?.copy(
            category = category
        )
        showCategorySelection.value = false
    }

    override fun onPayByDateChange(dateMillis: Long) {
        bill.value = bill.value?.copy(
            dateMillis = dateMillis
        )
    }

    override fun onSave() {
        viewModelScope.launch {
            val description = name.value?.trim().orEmpty()
            if (description.isEmpty()) {
                eventsChannel.send(AddEditBillEvent.ShowErrorMessage(UiText.StringResource(R.string.error_invalid_description)))
                return@launch
            }
            val amount = amount.value?.trim().orEmpty()
            if (amount.toDoubleOrZero() <= 0.0) {
                eventsChannel.send(AddEditBillEvent.ShowErrorMessage(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            bill.value?.let {
                repo.cacheBill(it)
                eventsChannel.send(
                    if (isEditMode) AddEditBillEvent.BillUpdated
                    else AddEditBillEvent.BillAdded
                )
            }
        }
    }

    override fun onDeleteClick() {
        showDeletionConfirmation.value = true
    }

    override fun onDeleteDismiss() {
        showDeletionConfirmation.value = false
    }

    override fun onDeleteConfirm() {
        billId?.let {
            viewModelScope.launch {
                repo.deleteBill(it)
                showDeletionConfirmation.value = false
                eventsChannel.send(AddEditBillEvent.BillDeleted)
            }
        }
    }

    sealed class AddEditBillEvent {
        data class ShowErrorMessage(val message: UiText) : AddEditBillEvent()
        object BillAdded : AddEditBillEvent()
        object BillUpdated : AddEditBillEvent()
        object BillDeleted : AddEditBillEvent()
    }
}

private const val KEY_BILL_LIVE_DATA = "KEY_BILL_LIVE_DATA"

const val ADD_EDIT_BILL_RESULT = "ADD_EDIT_BILL_RESULT"
const val RESULT_BILL_ADDED = "RESULT_BILL_ADDED"
const val RESULT_BILL_UPDATED = "RESULT_BILL_UPDATED"
const val RESULT_BILL_DELETED = "RESULT_BILL_DELETED"
