package com.budgetwise.expensetracker.domain.model

enum class ExpenseCategory(val label: String) {
    Food("Food"),
    Transport("Transport"),
    Shopping("Shopping"),
    Bills("Bills"),
    Entertainment("Entertainment"),
    Health("Health"),
    Other("Other");

    companion object {
        fun fromNote(note: String): ExpenseCategory {
            val normalized = note.lowercase()
            return when {
                "food" in normalized -> Food
                "uber" in normalized -> Transport
                "amazon" in normalized -> Shopping
                else -> Other
            }
        }
    }
}

