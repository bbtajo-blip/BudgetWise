package com.budgetwise.expensetracker.ui.screens.expenses

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetwise.expensetracker.domain.model.Expense
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import com.budgetwise.expensetracker.domain.model.ExpenseFilter
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Night)
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text("Expenses History", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND)
                        .setType("text/csv")
                        .putExtra(Intent.EXTRA_SUBJECT, "Expense export")
                        .putExtra(Intent.EXTRA_TEXT, state.csv)
                    context.startActivity(Intent.createChooser(intent, "Export CSV"))
                },
                enabled = state.expenses.isNotEmpty(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentLime)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(AccentLime)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")), color = Night, fontWeight = FontWeight.ExtraBold)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ExpenseFilter.entries.forEach { filter ->
                FilterChip(
                    selected = state.filter == filter,
                    onClick = { viewModel.onFilterChanged(filter) },
                    label = { Text(filter.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Panel,
                        labelColor = TextMuted,
                        selectedContainerColor = PanelSoft,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = state.filter == filter,
                        borderColor = PanelSoft,
                        selectedBorderColor = AccentLime
                    )
                )
            }
        }

        when {
            state.isLoading -> CircularProgressIndicator(color = AccentLime)
            state.expenses.isEmpty() -> EmptyState()
            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                state.groupedExpenses.forEach { (date, expenses) ->
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(date.format(DateTimeFormatter.ofPattern("EEE, MMM d")), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp)
                                    .height(3.dp)
                                    .background(AccentLime, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                    items(expenses, key = { it.id }) { expense ->
                        ExpenseRow(expense)
                    }
                }
                item { Box(modifier = Modifier.size(88.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Panel)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = AccentLime, modifier = Modifier.size(42.dp))
        Text("No expenses yet", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Add your first expense to see history grouped by date.", color = TextMuted)
    }
}

@Composable
private fun ExpenseRow(expense: Expense) {
    val currency = NumberFormat.getCurrencyInstance()
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
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(categoryColor(expense.category).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(categoryIcon(expense.category), contentDescription = null, tint = categoryColor(expense.category), modifier = Modifier.size(28.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(expense.category.label, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(expense.note.ifBlank { "Little note" }, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(currency.format(expense.amount), color = Color(0xFFFF8FA3), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
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
    ExpenseCategory.Transport -> Icons.Default.TrendingUp
    ExpenseCategory.Shopping -> Icons.Default.ShoppingBag
    ExpenseCategory.Bills -> Icons.Default.Bolt
    ExpenseCategory.Entertainment -> Icons.Default.ReceiptLong
    ExpenseCategory.Health -> Icons.Default.TrendingUp
    ExpenseCategory.Other -> Icons.Default.ReceiptLong
}




