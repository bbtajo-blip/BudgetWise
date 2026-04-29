package com.budgetwise.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.budgetwise.expensetracker.ui.navigation.BudgetWiseApp
import com.budgetwise.expensetracker.ui.theme.BudgetWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetWiseTheme {
                BudgetWiseApp()
            }
        }
    }
}

