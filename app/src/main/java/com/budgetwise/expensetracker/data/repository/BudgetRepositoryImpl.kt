package com.budgetwise.expensetracker.data.repository

import android.content.SharedPreferences
import com.budgetwise.expensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val preferences: SharedPreferences
) : BudgetRepository {
    override val monthlyBudget: Flow<Double> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_MONTHLY_BUDGET) trySend(preferences.getFloat(KEY_MONTHLY_BUDGET, 0f).toDouble())
        }
        trySend(preferences.getFloat(KEY_MONTHLY_BUDGET, 0f).toDouble())
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun setMonthlyBudget(amount: Double) {
        preferences.edit().putFloat(KEY_MONTHLY_BUDGET, amount.toFloat()).apply()
    }

    private companion object {
        const val KEY_MONTHLY_BUDGET = "monthly_budget"
    }
}

