package com.xpenses.android.feature_bills.presentation.bills_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.xpenses.android.R
import com.xpenses.android.core.domain.model.UiText
import com.xpenses.android.feature_bills.domain.model.BillPayment
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import com.xpenses.android.feature_bills.presentation.add_bill.RESULT_BILL_ADDED
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsListViewModel @Inject constructor(
    private val repo: BillsRepository
) : ViewModel(), BillsListActions {

    private val bills = repo.getBills()
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
            else -> return
        }
        viewModelScope.launch {
            eventsChannel.send(BillsListEvent.ShowSnackbar(UiText.StringResource(message)))
        }
    }

    sealed class BillsListEvent {
        data class ShowSnackbar(val message: UiText) : BillsListEvent()
    }
}