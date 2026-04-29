package com.budgetwise.expensetracker.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetwise.expensetracker.domain.model.ExpenseCategory
import com.budgetwise.expensetracker.ui.theme.AccentBlue
import com.budgetwise.expensetracker.ui.theme.AccentGreen
import com.budgetwise.expensetracker.ui.theme.AccentLime
import com.budgetwise.expensetracker.ui.theme.Night
import com.budgetwise.expensetracker.ui.theme.Panel
import com.budgetwise.expensetracker.ui.theme.PanelSoft
import com.budgetwise.expensetracker.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

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
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text("Add Expense", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PanelSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = AccentLime)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Brush.linearGradient(listOf(AccentBlue, AccentGreen)))
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("New transaction", color = Night.copy(alpha = 0.72f), style = MaterialTheme.typography.titleMedium)
                Text(if (state.amount.isBlank()) "\$0.00" else "\$${state.amount}", color = Color.White, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold)
                Text("${state.category.label} - ${state.date}", color = Color.White.copy(alpha = 0.82f))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Panel)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FinanceTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChanged,
                label = "Amount",
                leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                keyboardType = KeyboardType.Decimal
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.category.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(PanelSoft)
                ) {
                    ExpenseCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.label, color = Color.White) },
                            onClick = {
                                viewModel.onCategoryChanged(category)
                                expanded = false
                            }
                        )
                    }
                }
            }

            FinanceTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChanged,
                label = "Note",
                leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = null) }
            )

            FinanceTextField(
                value = state.date,
                onValueChange = viewModel::onDateChanged,
                label = "Date",
                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                supportingText = "YYYY-MM-DD"
            )
        }

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
        if (state.saved) {
            Text("Expense saved", color = AccentLime, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = viewModel::save,
            enabled = !state.isSaving,
            colors = ButtonDefaults.buttonColors(containerColor = AccentLime, contentColor = Night),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 88.dp)
        ) {
            Text(if (state.isSaving) "Saving..." else "Save expense", fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun FinanceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        supportingText = supportingText?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = fieldColors(),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = PanelSoft,
    unfocusedContainerColor = PanelSoft,
    focusedBorderColor = AccentLime,
    unfocusedBorderColor = PanelSoft,
    focusedLabelColor = AccentLime,
    unfocusedLabelColor = TextMuted,
    focusedLeadingIconColor = AccentLime,
    unfocusedLeadingIconColor = TextMuted,
    focusedSupportingTextColor = TextMuted,
    unfocusedSupportingTextColor = TextMuted
)


