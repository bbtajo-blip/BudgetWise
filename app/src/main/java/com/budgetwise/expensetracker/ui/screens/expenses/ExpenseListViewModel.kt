package com.budgetwise.expensetracker.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.model.ExpenseFilter
import com.budgetwise.expensetracker.domain.usecase.ExportExpensesCsvUseCase
import com.budgetwise.expensetracker.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class ExpenseListUiState(
    val isLoading: Boolean = true,
    val filter: ExpenseFilter = ExpenseFilter.Monthly,
    val expenses: List<Expense> = emptyList(),
    val groupedExpenses: Map<LocalDate, List<Expense>> = emptyMap(),
    val csv: String = ""
)

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    getExpenses: GetExpensesUseCase,
    private val exportCsv: ExportExpensesCsvUseCase
) : ViewModel() {
    private val filter = MutableStateFlow(ExpenseFilter.Monthly)

    val uiState: StateFlow<ExpenseListUiState> =
        combine(getExpenses(), filter) { expenses, currentFilter ->
            val now = LocalDate.now()
            val visible = when (currentFilter) {
                ExpenseFilter.Daily -> expenses.filter { it.date == now }
                ExpenseFilter.Monthly -> {
                    val month = YearMonth.now()
                    expenses.filter { YearMonth.from(it.date) == month }
                }
            }
            ExpenseListUiState(
                isLoading = false,
                filter = currentFilter,
                expenses = visible,
                groupedExpenses = visible.groupBy { it.date },
                csv = exportCsv(visible)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExpenseListUiState()
        )

    fun onFilterChanged(value: ExpenseFilter) {
        filter.update { value }
    }
}

