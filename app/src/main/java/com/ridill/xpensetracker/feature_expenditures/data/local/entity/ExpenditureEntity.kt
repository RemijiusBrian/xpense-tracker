package com.ridill.xpensetracker.feature_expenditures.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory

@Entity
data class ExpenditureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val dateMillis: Long = System.currentTimeMillis(),
    val isMonthly: Boolean = false,
    val category: String,
) {
    fun toExpenditure(): Expenditure = Expenditure(
        id = id,
        name = name,
        amount = amount,
        dateMillis = dateMillis,
        isMonthly = isMonthly,
        category = ExpenditureCategory.valueOf(category)
    )
}