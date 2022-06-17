package com.xpenses.android.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xpenses.android.feature_bills.data.local.entity.BillEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ExpenseTagEntity::class,
            parentColumns = ["name"],
            childColumns = ["tag"]
        ),
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["id"],
            childColumns = ["billId"]
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
    val tag: String? = null,
    val billId: Long? = null
)