package com.budgetwise.expensetracker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.budgetwise.expensetracker.ui.screens.add.AddExpenseScreen
import com.budgetwise.expensetracker.ui.screens.dashboard.DashboardScreen
import com.budgetwise.expensetracker.ui.screens.expenses.ExpenseListScreen
import com.budgetwise.expensetracker.ui.theme.AccentLime
import com.budgetwise.expensetracker.ui.theme.Night
import com.budgetwise.expensetracker.ui.theme.Panel
import com.budgetwise.expensetracker.ui.theme.TextMuted

private enum class AppRoute(val route: String, val icon: ImageVector) {
    Dashboard("dashboard", Icons.Default.BarChart),
    Expenses("expenses", Icons.Default.ReceiptLong),
    Add("add", Icons.Default.Add)
}

@Composable
fun BudgetWiseApp() {
    val navController = rememberNavController()
    val tabs = listOf(AppRoute.Dashboard, AppRoute.Expenses, AppRoute.Add)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        containerColor = Night,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Night)
                    .navigationBarsPadding()
                    .padding(start = 56.dp, end = 56.dp, top = 10.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(Panel.copy(alpha = 0.94f))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        IconButton(
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (selected) AccentLime else Color.Transparent)
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.route,
                                tint = if (selected) Night else TextMuted
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Dashboard.route) { DashboardScreen() }
            composable(AppRoute.Expenses.route) { ExpenseListScreen() }
            composable(AppRoute.Add.route) { AddExpenseScreen() }
        }
    }
}




