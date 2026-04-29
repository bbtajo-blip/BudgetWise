package com.budgetwise.expensetracker.data.repository

import com.budgetwise.expensetracker.data.db.ExpenseDao
import com.budgetwise.expensetracker.data.db.toEntity
import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {
    override fun getExpenses(): Flow<List<Expense>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun getExpensesForMonth(month: YearMonth): Flow<List<Expense>> {
        val start = month.atDay(1)
        val end = month.atEndOfMonth()
        return dao.observeBetween(start.toEpochDay(), end.toEpochDay()).map { rows ->
            rows.map { it.toDomain() }
        }
    }

    override suspend fun addExpense(expense: Expense) = dao.insert(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) = dao.delete(expense.toEntity())
}

