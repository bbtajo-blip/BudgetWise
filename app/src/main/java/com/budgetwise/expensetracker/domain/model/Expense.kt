package com.budgetwise.expensetracker.domain.model

import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val amount: Double,
    val category: ExpenseCategory,
    val note: String,
    val date: LocalDate
)

