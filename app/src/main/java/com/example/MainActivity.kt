package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.repository.JuddlesRepository
import com.example.ui.components.JuddlesBackgroundWrapper
import com.example.ui.components.JuddlesFloatingBottomBar
import com.example.ui.components.MainNavTab
import com.example.ui.screens.BudgetFormDialog
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.GoalFormDialog
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.TransactionFormDialog
import com.example.ui.screens.WelcomeAuthScreen
import com.example.ui.screens.signOutJuddles
import com.example.ui.theme.JuddlesEvergreen
import com.example.ui.theme.JuddlesTheme
import com.example.ui.viewmodel.JuddlesViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

enum class SignedInDestination {
  HOME,
  GOALS,
  EXPENSES,
  REPORTS,
  PROFILE
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      JuddlesAppRoot()
    }
  }
}

@Composable
fun JuddlesAppRoot() {
  var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

  DisposableEffect(Unit) {
    val listener = FirebaseAuth.AuthStateListener { auth ->
      currentUser = auth.currentUser
    }
    Firebase.auth.addAuthStateListener(listener)
    onDispose {
      Firebase.auth.removeAuthStateListener(listener)
    }
  }

  val user = currentUser
  if (user == null) {
    JuddlesTheme(themeMode = "LIGHT") {
      WelcomeAuthScreen(
        onAuthSuccess = { currentUser = Firebase.auth.currentUser }
      )
    }
  } else {
    val uid = user.uid
    val displayName = user.displayName ?: "Explorer"
    val email = user.email ?: ""
    key(uid) {
      val juddlesViewModel: JuddlesViewModel = viewModel(
        key = uid,
        factory = viewModelFactory {
          initializer {
            val app = checkNotNull(this[APPLICATION_KEY]) {
              "APPLICATION_KEY missing from CreationExtras"
            }
            val databaseId = app.getString(R.string.firestore_database_id)
            val db = FirebaseFirestore.getInstance(databaseId)
            JuddlesViewModel(
              repository = JuddlesRepository(db),
              currentUserId = uid,
              initialDisplayName = displayName,
              initialEmail = email
            )
          }
        }
      )
      SignedInJuddlesWorkspace(viewModel = juddlesViewModel)
    }
  }
}

@Composable
fun SignedInJuddlesWorkspace(viewModel: JuddlesViewModel) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember(context) { CredentialManager.create(context) }
  val snackbarHostState = remember { SnackbarHostState() }

  var currentDestination by remember { mutableStateOf(SignedInDestination.HOME) }

  // Active modals
  var showAddTxType by remember { mutableStateOf<String?>(null) }
  var editingTransaction by remember { mutableStateOf<FinancialTransaction?>(null) }
  var showGoalModal by remember { mutableStateOf(false) }
  var editingGoal by remember { mutableStateOf<FinancialGoal?>(null) }
  var showBudgetModal by remember { mutableStateOf(false) }
  var showNotificationsModal by remember { mutableStateOf(false) }

  // BackHandler for non-Home destinations
  BackHandler(enabled = currentDestination != SignedInDestination.HOME) {
    currentDestination = when (currentDestination) {
      SignedInDestination.REPORTS -> SignedInDestination.EXPENSES
      else -> SignedInDestination.HOME
    }
  }

  LaunchedEffect(state.actionFeedback, state.errorMessage) {
    val msg = state.errorMessage ?: state.actionFeedback
    if (msg != null) {
      snackbarHostState.showSnackbar(msg)
      viewModel.clearFeedback()
    }
  }

  val selectedNavTab = when (currentDestination) {
    SignedInDestination.HOME -> MainNavTab.HOME
    SignedInDestination.EXPENSES, SignedInDestination.REPORTS -> MainNavTab.STATS
    SignedInDestination.GOALS -> MainNavTab.GOALS
    SignedInDestination.PROFILE -> MainNavTab.PROFILE
  }

  JuddlesTheme(themeMode = state.profile.themeMode) {
    Scaffold(
      contentWindowInsets = WindowInsets(0, 0, 0, 0),
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      containerColor = Color.Transparent
    ) { innerPadding ->
      JuddlesBackgroundWrapper(
        modifier = Modifier.padding(innerPadding)
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
        ) {
          when (currentDestination) {
            SignedInDestination.HOME -> {
              HomeDashboardScreen(
                state = state,
                onAddIncomeClick = { showAddTxType = "INCOME" },
                onAddExpenseClick = { showAddTxType = "EXPENSE" },
                onOpenGoalsClick = { currentDestination = SignedInDestination.GOALS },
                onOpenMoreToolsClick = { currentDestination = SignedInDestination.REPORTS },
                onAddTransactionClick = { showAddTxType = "EXPENSE" },
                onSetBudgetClick = { showBudgetModal = true },
                onNewGoalClick = {
                  editingGoal = null
                  showGoalModal = true
                },
                onViewReportsClick = { currentDestination = SignedInDestination.REPORTS },
                onEditMonthlyGoalClick = { currentDestination = SignedInDestination.PROFILE },
                onNotificationsClick = { showNotificationsModal = true },
                onToggleHideAmounts = { viewModel.toggleHideAmounts() },
                onLoadSampleWorkspace = { viewModel.loadSampleWorkspace() },
                onClearSampleWorkspace = { viewModel.clearAllWorkspaceData() }
              )
            }

            SignedInDestination.GOALS -> {
              GoalsScreen(
                state = state,
                onBackClick = { currentDestination = SignedInDestination.HOME },
                onCreateNewGoalClick = {
                  editingGoal = null
                  showGoalModal = true
                },
                onGoalClick = { goal ->
                  editingGoal = goal
                  showGoalModal = true
                },
                onEditMonthlyTargetClick = { currentDestination = SignedInDestination.PROFILE }
              )
            }

            SignedInDestination.EXPENSES -> {
              ExpensesScreen(
                state = state,
                onBackClick = { currentDestination = SignedInDestination.HOME },
                onSwitchToReportsClick = { currentDestination = SignedInDestination.REPORTS },
                onPeriodSelect = { viewModel.setPeriod(it) },
                onBudgetCardClick = { showBudgetModal = true },
                onTransactionClick = { tx -> editingTransaction = tx },
                onSearchQueryChange = { viewModel.setSearchQuery(it) }
              )
            }

            SignedInDestination.REPORTS -> {
              ReportsScreen(
                state = state,
                onBackClick = { currentDestination = SignedInDestination.EXPENSES },
                onShareExportClick = {
                  val csv = viewModel.buildExportCsvSummary()
                  val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Juddles Financial Summary")
                    putExtra(Intent.EXTRA_TEXT, csv)
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "Share Juddles Report"))
                },
                onSelectReportsTab = { viewModel.setReportsTab(it) }
              )
            }

            SignedInDestination.PROFILE -> {
              ProfileSettingsScreen(
                state = state,
                onUpdateProfile = { viewModel.updateProfile(it) },
                onExportCsvClick = {
                  val csv = viewModel.buildExportCsvSummary()
                  val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Juddles Financial Summary")
                    putExtra(Intent.EXTRA_TEXT, csv)
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "Share Juddles Report"))
                },
                onLoadSampleWorkspace = { viewModel.loadSampleWorkspace() },
                onClearAllData = { viewModel.clearAllWorkspaceData() },
                onSignOutClick = {
                  signOutJuddles(credentialManager = credentialManager, scope = scope)
                }
              )
            }
          }

          // Floating Rounded Capsule Navigation Bar above the bottom safe area
          JuddlesFloatingBottomBar(
            selectedTab = selectedNavTab,
            onSelectTab = { tab ->
              currentDestination = when (tab) {
                MainNavTab.HOME -> SignedInDestination.HOME
                MainNavTab.STATS -> {
                  // Toggle between Expenses and Reports when tapping Stats again
                  if (currentDestination == SignedInDestination.EXPENSES) {
                    SignedInDestination.REPORTS
                  } else {
                    SignedInDestination.EXPENSES
                  }
                }
                MainNavTab.GOALS -> SignedInDestination.GOALS
                MainNavTab.PROFILE -> SignedInDestination.PROFILE
              }
            },
            onCentralAddClick = { showAddTxType = "EXPENSE" },
            modifier = Modifier.align(Alignment.BottomCenter)
          )
        }
      }
    }

    // Add / Edit Transaction Modal
    if (showAddTxType != null || editingTransaction != null) {
      TransactionFormDialog(
        initialType = showAddTxType ?: editingTransaction?.type ?: "EXPENSE",
        existingTransaction = editingTransaction,
        currencyCode = state.profile.preferredCurrency,
        onDismiss = {
          showAddTxType = null
          editingTransaction = null
        },
        onSave = { existingId, type, amountMinor, category, accountName, targetAccount, note, merchant, recurring, frequency ->
          viewModel.saveTransaction(
            existingId = existingId,
            type = type,
            amountMinor = amountMinor,
            category = category,
            accountName = accountName,
            targetAccountName = targetAccount,
            note = note,
            merchant = merchant,
            recurring = recurring,
            frequency = frequency
          )
        },
        onDelete = { id -> viewModel.deleteTransaction(id) }
      )
    }

    // Create / Edit / Contribute Goal Modal
    if (showGoalModal) {
      GoalFormDialog(
        existingGoal = editingGoal,
        currencyCode = state.profile.preferredCurrency,
        onDismiss = {
          showGoalModal = false
          editingGoal = null
        },
        onSaveGoal = { existingId, title, targetMinor, currentMinor, targetDate, iconKey, notes ->
          viewModel.saveGoal(
            existingId = existingId,
            title = title,
            targetMinor = targetMinor,
            currentMinor = currentMinor,
            targetDate = targetDate,
            iconKey = iconKey,
            notes = notes
          )
        },
        onAddContribution = { goal, addMinor ->
          viewModel.contributeToGoal(goal, addMinor)
        },
        onDeleteGoal = { id -> viewModel.deleteGoal(id) }
      )
    }

    // Configure Budget Modal
    if (showBudgetModal) {
      BudgetFormDialog(
        existingBudget = state.budgets.firstOrNull(),
        currencyCode = state.profile.preferredCurrency,
        onDismiss = { showBudgetModal = false },
        onSaveBudget = { existingId, name, periodType, category, limitMinor, rolloverEnabled, alertThresholdPercent ->
          viewModel.saveBudget(
            existingId = existingId,
            name = name,
            periodType = periodType,
            category = category,
            limitMinor = limitMinor,
            rolloverEnabled = rolloverEnabled,
            alertThresholdPercent = alertThresholdPercent
          )
        }
      )
    }

    // Alerts & Notifications Status Modal
    if (showNotificationsModal) {
      val activeBudget = state.budgets.firstOrNull()
      val spent = FinancialCalculator.totalExpensesMinor(
        FinancialCalculator.filterByPeriod(state.transactions, state.selectedPeriod)
      )
      val limit = activeBudget?.limitMinor ?: 200000L
      val pct = FinancialCalculator.budgetUsagePercent(limit, spent)
      val completedGoals = state.goals.count { it.currentMinor >= it.targetMinor || it.status == "COMPLETED" }

      AlertDialog(
        onDismissRequest = { showNotificationsModal = false },
        title = { Text("Juddles Smart Alerts") },
        text = {
          Text(
            "• Budget status: $pct% of your ${
              FinancialCalculator.formatCurrencyMinor(limit, state.profile.preferredCurrency, includeCents = false)
            } limit used.\n" +
              "• Completed goals: $completedGoals of ${state.goals.size} goals reached.\n" +
              "• Alert threshold: ${activeBudget?.alertThresholdPercent ?: state.profile.budgetAlertThreshold}%."
          )
        },
        confirmButton = {
          Button(
            onClick = { showNotificationsModal = false },
            colors = ButtonDefaults.buttonColors(containerColor = JuddlesEvergreen)
          ) {
            Text("Got it")
          }
        },
        dismissButton = {
          TextButton(
            onClick = {
              showNotificationsModal = false
              currentDestination = SignedInDestination.PROFILE
            }
          ) {
            Text("Settings")
          }
        }
      )
    }
  }
}
