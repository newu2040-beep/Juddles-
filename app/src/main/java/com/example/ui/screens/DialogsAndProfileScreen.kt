package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.model.SpendingBudget
import com.example.data.model.UserProfile
import com.example.ui.theme.JuddlesCoral
import com.example.ui.theme.JuddlesEvergreen
import com.example.ui.theme.JuddlesMint
import com.example.ui.viewmodel.JuddlesDashboardState
import kotlin.math.roundToLong

/**
 * Profile & Personalization Screen (PDF Section 8: Currency, Theme, Compact Mode,
 * Notification Thresholds, Data Export, Sample Workspace control, and Sign-Out).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSettingsScreen(
  state: JuddlesDashboardState,
  onUpdateProfile: (UserProfile) -> Unit,
  onExportCsvClick: () -> Unit,
  onLoadSampleWorkspace: () -> Unit,
  onClearAllData: () -> Unit,
  onSignOutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showClearConfirm by remember { mutableStateOf(false) }
  var displayNameInput by remember(state.profile.displayName) { mutableStateOf(state.profile.displayName) }
  var openingBalanceInput by remember(state.profile.openingBalanceMinor) {
    mutableStateOf((state.profile.openingBalanceMinor / 100.0).toString())
  }
  var monthlyGoalInput by remember(state.profile.monthlyGoalMinor) {
    mutableStateOf((state.profile.monthlyGoalMinor / 100.0).toString())
  }

  val currencies = listOf("USD", "EUR", "GBP", "INR", "CAD", "AUD")
  val themes = listOf("LIGHT", "DARK", "SYSTEM")

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      end = 20.dp,
      top = 16.dp,
      bottom = 104.dp
    ),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Text(
        text = "Profile & Settings",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    // Account Card
    item {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(JuddlesMint),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = JuddlesEvergreen,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = state.profile.displayName.ifBlank { "Explorer" },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              if (state.profile.email.isNotBlank()) {
                Text(
                  text = state.profile.email,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          OutlinedTextField(
            value = displayNameInput,
            onValueChange = { displayNameInput = it },
            label = { Text("Display Name") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = openingBalanceInput,
              onValueChange = { openingBalanceInput = it },
              label = { Text("Opening Balance") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
              value = monthlyGoalInput,
              onValueChange = { monthlyGoalInput = it },
              label = { Text("Monthly Goal") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = {
              val openMinor = ((openingBalanceInput.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong()
              val goalMinor = ((monthlyGoalInput.toDoubleOrNull() ?: 5000.0) * 100.0).roundToLong()
              onUpdateProfile(
                state.profile.copy(
                  displayName = displayNameInput.ifBlank { "Explorer" },
                  openingBalanceMinor = openMinor,
                  monthlyGoalMinor = goalMinor
                )
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Save Profile & Balances")
          }
        }
      }
    }

    // Currency & Theme Card
    item {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Primary Currency",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(8.dp))
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            currencies.forEach { curr ->
              val selected = state.profile.preferredCurrency == curr
              FilterChip(
                selected = selected,
                onClick = { onUpdateProfile(state.profile.copy(preferredCurrency = curr)) },
                label = { Text(curr) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = JuddlesEvergreen,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Theme Appearance",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            themes.forEach { mode ->
              val selected = state.profile.themeMode == mode
              FilterChip(
                selected = selected,
                onClick = { onUpdateProfile(state.profile.copy(themeMode = mode)) },
                label = { Text(mode) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = JuddlesEvergreen,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Compact Mode", style = MaterialTheme.typography.titleSmall)
              Text(
                "Reduce vertical spacing while keeping 48dp touch targets",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = state.profile.compactMode,
              onCheckedChange = { onUpdateProfile(state.profile.copy(compactMode = it)) }
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Hide Sensitive Amounts", style = MaterialTheme.typography.titleSmall)
              Text(
                "Mask currency values across dashboard and cards",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = state.profile.hideAmounts,
              onCheckedChange = { onUpdateProfile(state.profile.copy(hideAmounts = it)) }
            )
          }
        }
      }
    }

    // Data Export, Sample Workspace & Sign Out Card
    item {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onExportCsvClick,
            colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("export_csv_profile_button")
          ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Export & Share Financial Summary (CSV)")
          }

          OutlinedButton(
            onClick = onLoadSampleWorkspace,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Load Labelled Sample Workspace", color = JuddlesEvergreen)
          }

          OutlinedButton(
            onClick = { showClearConfirm = true },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = JuddlesCoral)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear All Workspace Records", color = JuddlesCoral)
          }

          Button(
            onClick = onSignOutClick,
            colors = ButtonDefaults.buttonColors(containerColor = JuddlesCoral),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sign_out_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out")
          }
        }
      }
    }
  }

  if (showClearConfirm) {
    AlertDialog(
      onDismissRequest = { showClearConfirm = false },
      title = { Text("Clear All Records?") },
      text = {
        Text("This permanently deletes all transactions, goals, and budgets in your workspace. This action cannot be undone.")
      },
      confirmButton = {
        Button(
          onClick = {
            showClearConfirm = false
            onClearAllData()
          },
          colors = ButtonDefaults.buttonColors(containerColor = JuddlesCoral)
        ) {
          Text("Delete All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearConfirm = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Add or Edit Transaction Dialog (Supports EXPENSE, INCOME, TRANSFER, and Recurring frequency).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFormDialog(
  initialType: String = "EXPENSE",
  existingTransaction: FinancialTransaction? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSave: (
    existingId: String,
    type: String,
    amountMinor: Long,
    category: String,
    accountName: String,
    targetAccountName: String,
    note: String,
    merchant: String,
    recurring: Boolean,
    frequency: String
  ) -> Unit,
  onDelete: ((String) -> Unit)? = null
) {
  var type by remember { mutableStateOf(existingTransaction?.type ?: initialType) }
  var amountText by remember {
    mutableStateOf(
      if (existingTransaction != null) (existingTransaction.amountMinor / 100.0).toString() else ""
    )
  }
  val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Health", "Entertainment")
  val incomeCategories = listOf("Salary", "Freelance", "Investment", "Gift")
  val defaultCat = existingTransaction?.category
    ?: if (type == "INCOME") "Salary" else if (type == "TRANSFER") "Transfer" else "Food"

  var category by remember { mutableStateOf(defaultCat) }
  var accountName by remember { mutableStateOf(existingTransaction?.accountName ?: "Main Wallet") }
  var targetAccountName by remember { mutableStateOf(existingTransaction?.targetAccountName ?: "Savings Vault") }
  var note by remember { mutableStateOf(existingTransaction?.note ?: "") }
  var merchant by remember { mutableStateOf(existingTransaction?.merchant ?: "") }
  var recurring by remember { mutableStateOf(existingTransaction?.recurring ?: false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (existingTransaction == null) "Add Transaction" else "Edit Transaction",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Type selector: EXPENSE | INCOME | TRANSFER
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("EXPENSE", "INCOME", "TRANSFER").forEach { t ->
            FilterChip(
              selected = type == t,
              onClick = {
                type = t
                category = when (t) {
                  "INCOME" -> "Salary"
                  "TRANSFER" -> "Transfer"
                  else -> "Food"
                }
              },
              label = { Text(t) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = JuddlesEvergreen,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("Amount ($currencyCode)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_amount_input")
        )

        if (type != "TRANSFER") {
          Text("Category", style = MaterialTheme.typography.labelLarge)
          val cats = if (type == "INCOME") incomeCategories else expenseCategories
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            cats.forEach { c ->
              FilterChip(
                selected = category == c,
                onClick = { category = c },
                label = { Text(c) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = JuddlesEvergreen,
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        } else {
          OutlinedTextField(
            value = targetAccountName,
            onValueChange = { targetAccountName = it },
            label = { Text("To Account") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }

        OutlinedTextField(
          value = merchant,
          onValueChange = { merchant = it },
          label = { Text("Merchant / Payee (Optional)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = note,
          onValueChange = { note = it },
          label = { Text("Note (Optional)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Recurring Monthly", style = MaterialTheme.typography.bodyMedium)
          Switch(checked = recurring, onCheckedChange = { recurring = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val parsed = amountText.toDoubleOrNull() ?: 0.0
          val minor = (parsed * 100.0).roundToLong().coerceAtLeast(1L)
          onSave(
            existingTransaction?.id ?: "",
            type,
            minor,
            category,
            accountName,
            if (type == "TRANSFER") targetAccountName else "",
            note,
            merchant,
            recurring,
            if (recurring) "MONTHLY" else "NONE"
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen),
        modifier = Modifier.testTag("save_transaction_button")
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      Row {
        if (existingTransaction != null && onDelete != null) {
          TextButton(
            onClick = {
              onDelete(existingTransaction.id)
              onDismiss()
            }
          ) {
            Text("Delete", color = JuddlesCoral)
          }
        }
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      }
    }
  )
}

/**
 * Create / Edit / Contribute to Goal Dialog.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalFormDialog(
  existingGoal: FinancialGoal? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSaveGoal: (
    existingId: String,
    title: String,
    targetMinor: Long,
    currentMinor: Long,
    targetDate: String,
    iconKey: String,
    notes: String
  ) -> Unit,
  onAddContribution: ((FinancialGoal, Long) -> Unit)? = null,
  onDeleteGoal: ((String) -> Unit)? = null
) {
  var title by remember { mutableStateOf(existingGoal?.title ?: "") }
  var targetText by remember {
    mutableStateOf(if (existingGoal != null) (existingGoal.targetMinor / 100.0).toString() else "5000")
  }
  var currentText by remember {
    mutableStateOf(if (existingGoal != null) (existingGoal.currentMinor / 100.0).toString() else "0")
  }
  var contributionText by remember { mutableStateOf("") }
  var targetDate by remember { mutableStateOf(existingGoal?.targetDate ?: "Dec 2027") }
  var iconKey by remember { mutableStateOf(existingGoal?.iconKey ?: "TRAVEL") }
  var notes by remember { mutableStateOf(existingGoal?.notes ?: "") }

  val icons = listOf("TRAVEL", "HOME", "CAR", "EMERGENCY", "EDUCATION", "TROPHY")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (existingGoal == null) "Create New Goal" else "Manage Goal",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (existingGoal != null && onAddContribution != null) {
          Surface(
            color = JuddlesMint,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = contributionText,
                onValueChange = { contributionText = it },
                label = { Text("+ Add Contribution ($currencyCode)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f)
              )
              Button(
                onClick = {
                  val addMinor = ((contributionText.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong()
                  if (addMinor > 0L) {
                    onAddContribution(existingGoal, addMinor)
                    onDismiss()
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen)
              ) {
                Text("Add")
              }
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Goal Title (e.g. Travel the World)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_title_input")
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = targetText,
            onValueChange = { targetText = it },
            label = { Text("Target ($currencyCode)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = currentText,
            onValueChange = { currentText = it },
            label = { Text("Saved ($currencyCode)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          )
        }

        OutlinedTextField(
          value = targetDate,
          onValueChange = { targetDate = it },
          label = { Text("Target Deadline") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        Text("Icon Category", style = MaterialTheme.typography.labelLarge)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          icons.forEach { ic ->
            FilterChip(
              selected = iconKey == ic,
              onClick = { iconKey = ic },
              label = { Text(ic) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = JuddlesEvergreen,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val tMinor = ((targetText.toDoubleOrNull() ?: 1000.0) * 100.0).roundToLong().coerceAtLeast(100L)
          val cMinor = ((currentText.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong().coerceAtLeast(0L)
          onSaveGoal(
            existingGoal?.id ?: "",
            title.ifBlank { "Savings Goal" },
            tMinor,
            cMinor,
            targetDate,
            iconKey,
            notes
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen),
        modifier = Modifier.testTag("save_goal_button")
      ) {
        Text("Save Goal")
      }
    },
    dismissButton = {
      Row {
        if (existingGoal != null && onDeleteGoal != null) {
          TextButton(
            onClick = {
              onDeleteGoal(existingGoal.id)
              onDismiss()
            }
          ) {
            Text("Delete", color = JuddlesCoral)
          }
        }
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      }
    }
  )
}

/**
 * Set / Edit Budget Dialog.
 */
@Composable
fun BudgetFormDialog(
  existingBudget: SpendingBudget? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSaveBudget: (
    existingId: String,
    name: String,
    periodType: String,
    category: String,
    limitMinor: Long,
    rolloverEnabled: Boolean,
    alertThresholdPercent: Long
  ) -> Unit
) {
  var name by remember { mutableStateOf(existingBudget?.name ?: "Monthly Budget") }
  var limitText by remember {
    mutableStateOf(
      if (existingBudget != null) (existingBudget.limitMinor / 100.0).toString() else "2000"
    )
  }
  var periodType by remember { mutableStateOf(existingBudget?.periodType ?: "MONTHLY") }
  var thresholdText by remember {
    mutableStateOf((existingBudget?.alertThresholdPercent ?: 80L).toString())
  }
  var rollover by remember { mutableStateOf(existingBudget?.rolloverEnabled ?: false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Configure Budget",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Budget Name") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = limitText,
          onValueChange = { limitText = it },
          label = { Text("Budget Limit ($currencyCode)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("budget_limit_input")
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("WEEKLY", "MONTHLY", "YEARLY").forEach { p ->
            FilterChip(
              selected = periodType == p,
              onClick = { periodType = p },
              label = { Text(p) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = JuddlesEvergreen,
                selectedLabelColor = Color.White
              )
            )
          }
        }
        OutlinedTextField(
          value = thresholdText,
          onValueChange = { thresholdText = it },
          label = { Text("Alert Threshold % (e.g. 80)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Rollover Unused Balance", style = MaterialTheme.typography.bodyMedium)
          Switch(checked = rollover, onCheckedChange = { rollover = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val limitMinor = ((limitText.toDoubleOrNull() ?: 2000.0) * 100.0).roundToLong().coerceAtLeast(100L)
          val threshold = (thresholdText.toLongOrNull() ?: 80L).coerceIn(10L, 100L)
          onSaveBudget(
            existingBudget?.id ?: "",
            name.ifBlank { "Monthly Budget" },
            periodType,
            "ALL",
            limitMinor,
            rollover,
            threshold
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen),
        modifier = Modifier.testTag("save_budget_button")
      ) {
        Text("Save Budget")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
