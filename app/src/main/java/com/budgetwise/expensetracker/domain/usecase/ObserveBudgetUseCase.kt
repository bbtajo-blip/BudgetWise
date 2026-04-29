package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.repository.BudgetRepository
import javax.inject.Inject

class ObserveBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke() = repository.monthlyBudget
}

