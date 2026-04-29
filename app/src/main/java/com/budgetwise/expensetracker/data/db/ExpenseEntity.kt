package com.budgetwise.expensetracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import java.time.LocalDate

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String,
    val note: String,
    val dateEpochDay: Long
) {
    fun toDomain() = Expense(
        id = id,
        amount = amount,
        category = ExpenseCategory.valueOf(category),
        note = note,
        date = LocalDate.ofEpochDay(dateEpochDay)
    )
}

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    amount = amount,
    category = category.name,
    note = note,
    dateEpochDay = date.toEpochDay()
)

