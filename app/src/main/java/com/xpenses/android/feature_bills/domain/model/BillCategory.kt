package com.xpenses.android.feature_bills.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.xpenses.android.R

enum class BillCategory(
    val icon: ImageVector,
    @StringRes val label: Int
) {
    ELECTRICITY(Icons.Outlined.ElectricBolt, R.string.bill_category_electricity),
    WATER(Icons.Outlined.WaterDrop, R.string.bill_category_water),
    BROADBAND(Icons.Outlined.Wifi, R.string.bill_category_broadband),
    DIGITAL_TV(Icons.Outlined.Tv, R.string.bill_category_dtv),
}