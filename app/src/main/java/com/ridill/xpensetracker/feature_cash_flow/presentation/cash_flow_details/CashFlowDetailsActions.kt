package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details

import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowDetailsOptions

interface CashFlowDetailsActions {
    fun onMenuOptionSelect(option: CashFlowDetailsOptions)
    fun onAgentNameChange(value: String)
    fun onDismissEditMode()
    fun onSaveAgent()
    fun onAddCashFlowClick()
    fun onAddEditCashFlowDismiss()
    fun onCashFlowNameChange(value: String)
    fun onCashFlowAmountChange(value: String)
    fun onCashFlowLendingChange(value: Boolean)
    fun onAddEditCashFlowConfirm(repaymentAmount: String)
    fun onCashFlowSwipeDelete(cashFlow: CashFlow)
    fun onUndoCashFlowDelete(cashFlow: CashFlow)
    fun onCashFlowClick(cashFlow: CashFlow)
    fun onClearCashFlowDismiss()
    fun onClearCashFlowConfirm()
}