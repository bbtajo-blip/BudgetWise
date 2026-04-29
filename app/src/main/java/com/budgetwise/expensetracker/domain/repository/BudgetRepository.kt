package com.budgetwise.expensetracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    val monthlyBudget: Flow<Double>
    suspend fun setMonthlyBudget(amount: Double)
}

