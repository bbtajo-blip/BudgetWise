package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetMonthlyExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(month: YearMonth): Flow<List<Expense>> = repository.getExpensesForMonth(month)
}

