package com.ridill.xpensetracker.feature_cash_flow.domain.model

import androidx.annotation.StringRes
import com.ridill.xpensetracker.R

enum class CashFlowDetailsOptions(@StringRes val label: Int) {
    EDIT(R.string.edit),
    STRIKE_OFF(R.string.strike_off)
}