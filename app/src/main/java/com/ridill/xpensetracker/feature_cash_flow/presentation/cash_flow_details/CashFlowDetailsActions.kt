package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowDetailsOptions

interface CashFlowDetailsActions {
    fun onMenuOptionSelect(option: CashFlowDetailsOptions)
    fun onExpenseNameChange(value: String)
    fun onDismissEditMode()
    fun onConfirmExpenseNameChange()
    fun onAddCashFlowClick()
    fun onDismissAddEditCashFlow()
    fun onCashFlowNameChange(value: String)
    fun onCashFlowAmountChange(value: String)
    fun onCashFlowLendingChange(value: Boolean)
    fun onAddEditCashFlowConfirm()
    fun onCashFlowSwipeDelete(cashFlow: CashFlow)
    fun onUndoCashFlowDelete(cashFlow: CashFlow)
    fun onCashFlowClick(cashFlow: CashFlow)
    fun onStrikeOffDismiss()
    fun onStrikeOffConfirm()
}