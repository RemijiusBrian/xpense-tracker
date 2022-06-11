package com.ridill.xpensetracker.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseTagEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String
)