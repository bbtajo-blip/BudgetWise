package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> = repository.getExpenses()
}

