package com.budgetwise.expensetracker.ui.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import com.budgetwise.expensetracker.ui.theme.AccentBlue
import com.budgetwise.expensetracker.ui.theme.AccentGreen
import com.budgetwise.expensetracker.ui.theme.AccentLime
import com.budgetwise.expensetracker.ui.theme.AccentOrange
import com.budgetwise.expensetracker.ui.theme.AccentPink
import com.budgetwise.expensetracker.ui.theme.AccentPurple
import com.budgetwise.expensetracker.ui.theme.Night
import com.budgetwise.expensetracker.ui.theme.Panel
import com.budgetwise.expensetracker.ui.theme.PanelSoft
import com.budgetwise.expensetracker.ui.theme.TextMuted
import java.text.NumberFormat

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currency = NumberFormat.getCurrencyInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Night)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Hello", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                Text("BudgetWise", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PanelSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = AccentLime)
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(color = AccentLime)
        } else {
            BalanceHero(state = state, currency = currency)
            SpendingRing(state = state, currency = currency)
            BudgetEditor(state = state, onBudgetInputChanged = viewModel::onBudgetInputChanged, onSave = viewModel::saveBudget)
            CategoryCards(values = state.categoryBreakdown, currency = currency)
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun BalanceHero(state: DashboardUiState, currency: NumberFormat) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = AccentLime),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Total spend this month", color = Night.copy(alpha = 0.7f), style = MaterialTheme.typography.titleMedium)
            Text(currency.format(state.totalMonthlySpending), color = Night, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatPill("Budget", if (state.budget > 0.0) currency.format(state.budget) else "Not set", Icons.Default.Savings, Modifier.weight(1f))
                StatPill("Status", if (state.isBudgetExceeded) "Exceeded" else "On track", Icons.Default.TrendingUp, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Night.copy(alpha = 0.13f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Night, modifier = Modifier.size(22.dp))
        Column {
            Text(label, color = Night.copy(alpha = 0.65f), style = MaterialTheme.typography.labelMedium)
            Text(value, color = Night, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SpendingRing(state: DashboardUiState, currency: NumberFormat) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Monthly budget", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = TextMuted)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(220.dp)) {
                    val stroke = 18.dp.toPx()
                    val diameter = size.minDimension - stroke
                    val topLeft = (size.minDimension - diameter) / 2f
                    drawArc(
                        color = PanelSoft,
                        startAngle = 150f,
                        sweepAngle = 240f,
                        useCenter = false,
                        topLeft = androidx.compose.ui.geometry.Offset(topLeft, topLeft),
                        size = Size(diameter, diameter),
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                    val colors = listOf(AccentBlue, AccentGreen, AccentOrange, AccentPink, AccentPurple, AccentLime)
                    val progress = if (state.budget > 0.0) (state.totalMonthlySpending / state.budget).toFloat().coerceIn(0f, 1f) else 0.72f
                    var start = 150f
                    val totalSweep = 240f * progress
                    colors.forEachIndexed { index, color ->
                        val segment = totalSweep / colors.size
                        drawArc(
                            color = color,
                            startAngle = start + index * segment,
                            sweepAngle = segment - 5f,
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(topLeft, topLeft),
                            size = Size(diameter, diameter),
                            style = Stroke(stroke, cap = StrokeCap.Round)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = AccentLime)
                    Text(currency.format(state.totalMonthlySpending), color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Text("spent", color = TextMuted)
                }
            }
            if (state.budget > 0.0) {
                LinearProgressIndicator(
                    progress = { (state.totalMonthlySpending / state.budget).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = if (state.isBudgetExceeded) MaterialTheme.colorScheme.error else AccentGreen,
                    trackColor = PanelSoft
                )
                Text(
                    text = if (state.isBudgetExceeded) "Your monthly budget has been exceeded" else "Your spending limit is ${currency.format(state.budget)}",
                    color = if (state.isBudgetExceeded) MaterialTheme.colorScheme.error else TextMuted
                )
            }
        }
    }
}

@Composable
private fun BudgetEditor(state: DashboardUiState, onBudgetInputChanged: (String) -> Unit, onSave: () -> Unit) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = PanelSoft), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Set monthly budget", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = state.budgetInput,
                onValueChange = onBudgetInputChanged,
                label = { Text("Budget amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = AccentLime,
                    unfocusedBorderColor = TextMuted,
                    focusedLabelColor = AccentLime,
                    unfocusedLabelColor = TextMuted
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = AccentLime, contentColor = Night),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save budget", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun CategoryCards(values: Map<ExpenseCategory, Double>, currency: NumberFormat) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Categories", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (values.isEmpty()) {
            Text("No spending yet this month.", color = TextMuted)
        } else {
            values.entries.sortedByDescending { it.value }.forEach { (category, amount) ->
                CategoryRow(category = category, amount = amount, currency = currency)
            }
        }
    }
}

@Composable
private fun CategoryRow(category: ExpenseCategory, amount: Double, currency: NumberFormat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Panel)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(categoryColor(category).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(categoryIcon(category), contentDescription = null, tint = categoryColor(category))
            }
            Column {
                Text(category.label, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Monthly spending", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(currency.format(amount), color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

private fun categoryColor(category: ExpenseCategory): Color = when (category) {
    ExpenseCategory.Food -> AccentOrange
    ExpenseCategory.Transport -> AccentBlue
    ExpenseCategory.Shopping -> AccentPink
    ExpenseCategory.Bills -> AccentPurple
    ExpenseCategory.Entertainment -> AccentLime
    ExpenseCategory.Health -> AccentGreen
    ExpenseCategory.Other -> TextMuted
}

private fun categoryIcon(category: ExpenseCategory): ImageVector = when (category) {
    ExpenseCategory.Food -> Icons.Default.LocalDining
    ExpenseCategory.Transport -> Icons.Default.ArrowDownward
    ExpenseCategory.Shopping -> Icons.Default.ShoppingBag
    ExpenseCategory.Bills -> Icons.Default.Bolt
    ExpenseCategory.Entertainment -> Icons.Default.Savings
    ExpenseCategory.Health -> Icons.Default.TrendingUp
    ExpenseCategory.Other -> Icons.Default.AccountBalanceWallet
}

