package com.ridill.xpensetracker.core.domain.model

import androidx.annotation.StringRes
import com.ridill.xpensetracker.R

enum class SortOrder(
    @StringRes val label: Int
) {
    ASCENDING(R.string.ascending),
    DESCENDING(R.string.descending)
}