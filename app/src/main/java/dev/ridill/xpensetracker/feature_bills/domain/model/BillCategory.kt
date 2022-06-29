package dev.ridill.xpensetracker.feature_bills.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.ridill.xpensetracker.R

enum class BillCategory(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    ELECTRICITY(R.drawable.ic_electricity, R.string.bill_category_electricity),
    WATER(R.drawable.ic_water_drops, R.string.bill_category_water),
    BROADBAND(R.drawable.ic_wifi_cloud, R.string.bill_category_broadband),
    DIGITAL_TV(R.drawable.ic_tv, R.string.bill_category_dtv),
    SUBSCRIPTION(R.drawable.ic_subscription, R.string.bill_category_subscription),
    MISC(R.drawable.ic_box, R.string.bill_category_misc)
}