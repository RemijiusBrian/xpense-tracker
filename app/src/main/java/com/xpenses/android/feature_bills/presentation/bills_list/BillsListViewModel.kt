package com.xpenses.android.feature_bills.presentation.bills_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BillsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: BillsRepository
) : ViewModel(), BillsListActions {

    private val bills = repo.getBills()
    private val upcomingBills = repo.getBills()

    val state = combineTuple(
        bills,
        upcomingBills
    ).map { (
                bills,
                upcomingBills
            ) ->
        BillsListState(
            billsList = bills
        )
    }.asLiveData()
}