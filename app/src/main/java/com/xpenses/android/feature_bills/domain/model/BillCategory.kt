package com.xpenses.android.feature_bills.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.xpenses.android.R

enum class BillCategory(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    ELECTRICITY(R.drawable.ic_electricity, R.string.bill_category_electricity),
    WATER(R.drawable.ic_water_drops, R.string.bill_category_water),
    BROADBAND(R.drawable.ic_wifi_cloud, R.string.bill_category_broadband),
    DIGITAL_TV(R.drawable.ic_tv, R.string.bill_category_dtv),
}