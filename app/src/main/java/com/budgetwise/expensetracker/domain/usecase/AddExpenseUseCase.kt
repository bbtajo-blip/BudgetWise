package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}

