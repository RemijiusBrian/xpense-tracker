package com.xpenses.android.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ExpenseTagEntity::class,
            parentColumns = ["name"],
            childColumns = ["tag"]
        )
    ],
    indices = [Index("tag")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Double,
    val dateMillis: Long,
    val tag: String? = null
)