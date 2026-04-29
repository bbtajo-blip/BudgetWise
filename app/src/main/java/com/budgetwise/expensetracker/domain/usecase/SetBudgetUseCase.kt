package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.repository.BudgetRepository
import javax.inject.Inject

class SetBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(amount: Double) = repository.setMonthlyBudget(amount)
}

