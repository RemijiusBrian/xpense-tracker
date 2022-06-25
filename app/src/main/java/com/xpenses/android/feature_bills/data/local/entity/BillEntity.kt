package com.xpenses.android.feature_bills.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Double,
    val payByDate: Long,
    val category: String,
    val recurring: Boolean
)