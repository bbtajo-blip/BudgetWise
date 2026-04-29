package com.budgetwise.expensetracker.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import com.budgetwise.expensetracker.domain.usecase.AddExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddExpenseUiState(
    val amount: String = "",
    val category: ExpenseCategory = ExpenseCategory.Other,
    val note: String = "",
    val date: String = LocalDate.now().toString(),
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpense: AddExpenseUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun onAmountChanged(value: String) {
        _uiState.update { it.copy(amount = value, error = null, saved = false) }
    }

    fun onCategoryChanged(value: ExpenseCategory) {
        _uiState.update { it.copy(category = value, error = null, saved = false) }
    }

    fun onNoteChanged(value: String) {
        val autoCategory = ExpenseCategory.fromNote(value)
        _uiState.update {
            it.copy(
                note = value,
                category = if (autoCategory != ExpenseCategory.Other) autoCategory else it.category,
                error = null,
                saved = false
            )
        }
    }

    fun onDateChanged(value: String) {
        _uiState.update { it.copy(date = value, error = null, saved = false) }
    }

    fun save() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull()
        val date = runCatching { LocalDate.parse(state.date) }.getOrNull()

        if (amount == null || amount <= 0.0) {
            _uiState.update { it.copy(error = "Enter a valid amount") }
            return
        }
        if (date == null) {
            _uiState.update { it.copy(error = "Use date format YYYY-MM-DD") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            addExpense(
                Expense(
                    amount = amount,
                    category = state.category,
                    note = state.note.trim(),
                    date = date
                )
            )
            _uiState.value = AddExpenseUiState(saved = true)
        }
    }
}

