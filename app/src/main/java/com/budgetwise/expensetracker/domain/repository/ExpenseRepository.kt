package com.budgetwise.expensetracker.domain.repository

import com.budgetwise.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    fun getExpensesForMonth(month: YearMonth): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}

