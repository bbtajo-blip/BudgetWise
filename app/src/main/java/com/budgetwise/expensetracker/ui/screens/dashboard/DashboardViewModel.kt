package com.budgetwise.expensetracker.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import com.budgetwise.expensetracker.domain.usecase.GetMonthlyExpensesUseCase
import com.budgetwise.expensetracker.domain.usecase.ObserveBudgetUseCase
import com.budgetwise.expensetracker.domain.usecase.SetBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val totalMonthlySpending: Double = 0.0,
    val categoryBreakdown: Map<ExpenseCategory, Double> = emptyMap(),
    val budget: Double = 0.0,
    val budgetInput: String = "",
    val isBudgetExceeded: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getMonthlyExpenses: GetMonthlyExpensesUseCase,
    observeBudget: ObserveBudgetUseCase,
    private val setBudget: SetBudgetUseCase
) : ViewModel() {
    private val budgetInput = MutableStateFlow("")

    val uiState: StateFlow<DashboardUiState> =
        combine(getMonthlyExpenses(YearMonth.now()), observeBudget(), budgetInput) { expenses, budget, input ->
            val total = expenses.sumOf { it.amount }
            DashboardUiState(
                isLoading = false,
                totalMonthlySpending = total,
                categoryBreakdown = expenses.groupBy { it.category }.mapValues { (_, items) -> items.sumOf { it.amount } },
                budget = budget,
                budgetInput = input.ifEmpty { if (budget > 0.0) budget.toString() else "" },
                isBudgetExceeded = budget > 0.0 && total > budget
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun onBudgetInputChanged(value: String) {
        budgetInput.update { value }
    }

    fun saveBudget() {
        val value = budgetInput.value.toDoubleOrNull() ?: return
        viewModelScope.launch {
            setBudget(value.coerceAtLeast(0.0))
        }
    }
}

