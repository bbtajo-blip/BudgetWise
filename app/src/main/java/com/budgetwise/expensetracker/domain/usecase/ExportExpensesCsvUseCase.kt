package com.budgetwise.expensetracker.domain.usecase

import com.budgetwise.expensetracker.domain.model.Expense
import java.util.Locale
import javax.inject.Inject

class ExportExpensesCsvUseCase @Inject constructor() {
    operator fun invoke(expenses: List<Expense>): String {
        val rows = expenses.joinToString(separator = "\n") { expense ->
            listOf(
                expense.date.toString(),
                String.format(Locale.US, "%.2f", expense.amount),
                expense.category.label,
                expense.note.escapeCsv()
            ).joinToString(",")
        }
        return "date,amount,category,note\n$rows"
    }

    private fun String.escapeCsv(): String {
        val escaped = replace("\"", "\"\"")
        return if (any { it == ',' || it == '\n' || it == '"' }) "\"$escaped\"" else escaped
    }
}

