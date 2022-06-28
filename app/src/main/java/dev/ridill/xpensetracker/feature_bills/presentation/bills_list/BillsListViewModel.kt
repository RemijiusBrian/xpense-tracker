package dev.ridill.xpensetracker.feature_bills.presentation.bills_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill.RESULT_BILL_ADDED
import dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill.RESULT_BILL_DELETED
import dev.ridill.xpensetracker.feature_bills.presentation.add_edit_bill.RESULT_BILL_UPDATED
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsListViewModel @Inject constructor(
    private val repo: BillsRepository
) : ViewModel(), BillsListActions {

    private val bills = repo.getBillsGroupedByCategory()
    private val billPayments = repo.getBillPaymentsForCurrentMonth()

    private val eventsChannel = Channel<BillsListEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    val state = combineTuple(
        bills,
        billPayments
    ).map { (
                bills,
                billPayments
            ) ->
        BillsListState(
            billsList = bills,
            billPayments = billPayments
        )
    }.asLiveData()

    override fun onMarkAsPaidClick(payment: BillPayment) {
        viewModelScope.launch {
            repo.markBillAsPaid(payment)
            eventsChannel.send(BillsListEvent.ShowSnackbar(UiText.StringResource(R.string.bill_marked_as_paid_message)))
        }
    }

    fun onAddBillResult(result: String) {
        val message = when (result) {
            RESULT_BILL_ADDED -> R.string.message_bill_added
            RESULT_BILL_UPDATED -> R.string.message_bill_updated
            RESULT_BILL_DELETED -> R.string.message_bill_deleted
            else -> return
        }
        viewModelScope.launch {
            eventsChannel.send(BillsListEvent.ShowSnackbar(UiText.StringResource(message)))
        }
    }

    override fun onBillClick(id: Long) {
        viewModelScope.launch {
            eventsChannel.send(BillsListEvent.NavigateToAddEditBillScreen(id))
        }
    }

    sealed class BillsListEvent {
        data class ShowSnackbar(val message: UiText) : BillsListEvent()
        data class NavigateToAddEditBillScreen(val id: Long) : BillsListEvent()
    }
}