package com.ridill.xpensetracker.feature_cash_flow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CashFlowAgentEntity::class,
            parentColumns = ["id"],
            childColumns = ["agent"]
        )
    ]
)
data class CashFlowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(index = true)
    val agent: Long,
    val name: String,
    val amount: Long,
    val date: Long,
    val lent: Boolean = true,
) {
    fun toCashFlow(): CashFlow = CashFlow(
        id = id,
        agent = agent,
        name = name,
        amount = amount,
        date = date,
        lending = lent
    )
}