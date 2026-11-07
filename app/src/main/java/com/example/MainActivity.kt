package com.example

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.example.ui.screens.CustomDateReminderDialog
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.ExportReportDialog
import com.example.ui.screens.GoalFormDialog
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.MoreToolsDialog
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.TransactionFormDialog
import com.example.ui.screens.WelcomeAuthScreen
import com.example.ui.screens.signOutJuddles
import com.example.ui.theme.JuddlesTheme
import com.example.ui.viewmodel.JuddlesViewModel
import com.example.util.CustomDateReminder
import com.example.util.ExportFormat
import com.example.util.JuddlesNotificationManager
import com.example.util.ReportExportManager
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
    JuddlesNotificationManager.ensureNotificationChannel(this)
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
    JuddlesTheme(themeMode = "LIGHT", compactMode = false, fontMode = "PLUS_JAKARTA") {
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
  val uid = state.profile.userId.ifBlank { "default_user" }

  var currentDestination by remember { mutableStateOf(SignedInDestination.HOME) }

  // Font, Reminders & Permissions State
  var currentFontMode by remember(uid) {
    mutableStateOf(JuddlesNotificationManager.loadFontPreference(context, uid))
  }
  var reminders by remember(uid) {
    mutableStateOf(JuddlesNotificationManager.loadReminders(context, uid))
  }
  var hapticsEnabled by remember(uid) {
    mutableStateOf(JuddlesNotificationManager.loadHapticsEnabled(context, uid))
  }
  var savedExportFolderUri by remember(uid) {
    mutableStateOf(JuddlesNotificationManager.loadSavedExportFolderUri(context, uid))
  }
  var hasNotificationPermission by remember {
    mutableStateOf(JuddlesNotificationManager.hasNotificationPermission(context))
  }

  // Active modals
  var showAddTxType by remember { mutableStateOf<String?>(null) }
  var editingTransaction by remember { mutableStateOf<FinancialTransaction?>(null) }
  var showGoalModal by remember { mutableStateOf(false) }
  var editingGoal by remember { mutableStateOf<FinancialGoal?>(null) }
  var showBudgetModal by remember { mutableStateOf(false) }
  var showNotificationsModal by remember { mutableStateOf(false) }
  var showMoreToolsModal by remember { mutableStateOf(false) }
  var showCustomReminderModal by remember { mutableStateOf(false) }
  var showExportModalFormat by remember { mutableStateOf<ExportFormat?>(null) }
  var pendingSaveFormat by remember { mutableStateOf(ExportFormat.PDF) }

  // Runtime Permission Launcher for POST_NOTIFICATIONS (Android 13+)
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    hasNotificationPermission = granted
    if (granted) {
      JuddlesNotificationManager.sendInstantNotification(
        context = context,
        title = "Juddles Push Notifications Enabled",
        message = "You will now receive custom date reminders, bill alerts & budget notifications.",
        vibrate = hapticsEnabled
      )
      viewModel.setFeedbackMessage("Push notifications allowed & test alert sent")
    } else {
      viewModel.setFeedbackMessage("Notification permission denied by device")
    }
  }

  // SAF Folder Access Launcher (OpenDocumentTree) for Files Access Permission
  val openDocumentTreeLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocumentTree()
  ) { treeUri ->
    if (treeUri != null) {
      runCatching {
        context.contentResolver.takePersistableUriPermission(
          treeUri,
          Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
      }
      val uriStr = treeUri.toString()
      savedExportFolderUri = uriStr
      JuddlesNotificationManager.saveExportFolderUri(context, uid, uriStr)
      viewModel.setFeedbackMessage("Files & folder export access granted")
    }
  }

  // Storage Access Framework (SAF) CreateDocument Launcher for saving PDF, CSV, TXT, and MD files
  val createDocumentLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CreateDocument("*/*")
  ) { uri ->
    if (uri != null) {
      val res = ReportExportManager.writeExportToUri(
        context = context,
        uri = uri,
        format = pendingSaveFormat,
        state = state
      )
      viewModel.setFeedbackMessage(
        res.getOrElse { "Export error: ${it.localizedMessage}" }
      )
    }
  }

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

  JuddlesTheme(
    themeMode = state.profile.themeMode,
    compactMode = state.profile.compactMode,
    fontMode = currentFontMode
  ) {
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
                onOpenMoreToolsClick = { showMoreToolsModal = true },
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
                onClearSampleWorkspace = { viewModel.clearAllWorkspaceData() },
                onOpenExpensesClick = { currentDestination = SignedInDestination.EXPENSES },
                onTransactionClick = { tx -> editingTransaction = tx }
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
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onCategoryFilterSelect = { viewModel.setCategoryFilter(it) }
              )
            }

            SignedInDestination.REPORTS -> {
              ReportsScreen(
                state = state,
                onBackClick = { currentDestination = SignedInDestination.EXPENSES },
                onShareExportClick = {
                  showExportModalFormat = ExportFormat.PDF
                },
                onSelectReportsTab = { viewModel.setReportsTab(it) }
              )
            }

            SignedInDestination.PROFILE -> {
              ProfileSettingsScreen(
                state = state,
                currentFontMode = currentFontMode,
                onSelectFontMode = { fontKey ->
                  currentFontMode = fontKey
                  JuddlesNotificationManager.saveFontPreference(context, uid, fontKey)
                  viewModel.setFeedbackMessage("Font updated to $fontKey")
                },
                reminders = reminders,
                onOpenAddReminderDialog = { showCustomReminderModal = true },
                onToggleReminderCompleted = { target ->
                  val updated = reminders.map {
                    if (it.id == target.id) it.copy(isCompleted = !it.isCompleted) else it
                  }
                  reminders = updated
                  JuddlesNotificationManager.saveReminders(context, uid, updated)
                },
                onDeleteReminder = { target ->
                  val updated = reminders.filterNot { it.id == target.id }
                  reminders = updated
                  JuddlesNotificationManager.saveReminders(context, uid, updated)
                  viewModel.setFeedbackMessage("Reminder deleted")
                },
                onSendReminderPushNow = { rem ->
                  if (!JuddlesNotificationManager.hasNotificationPermission(context) &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                  ) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                  } else {
                    val amtFmt = if (rem.amountMinor > 0L) {
                      " (${FinancialCalculator.formatCurrencyMinor(rem.amountMinor, state.profile.preferredCurrency)})"
                    } else ""
                    val sent = JuddlesNotificationManager.sendInstantNotification(
                      context = context,
                      title = "Juddles • ${rem.category}: ${rem.title}",
                      message = "Due on ${rem.targetDateText} at ${rem.targetTimeText}$amtFmt",
                      vibrate = hapticsEnabled
                    )
                    viewModel.setFeedbackMessage(
                      if (sent) "Push notification sent: ${rem.title}" else "Allow push notifications to receive alerts"
                    )
                  }
                },
                hasNotificationPermission = hasNotificationPermission,
                onRequestNotificationPermissionAndTest = {
                  if (!JuddlesNotificationManager.hasNotificationPermission(context) &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                  ) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                  } else {
                    hasNotificationPermission = true
                    JuddlesNotificationManager.sendInstantNotification(
                      context = context,
                      title = "Juddles v3.0 Push Alert",
                      message = "Push notifications & custom date reminders are active!",
                      vibrate = hapticsEnabled
                    )
                    viewModel.setFeedbackMessage("Test push notification delivered")
                  }
                },
                savedExportFolderUri = savedExportFolderUri,
                onRequestExportFolderAccess = {
                  openDocumentTreeLauncher.launch(null)
                },
                hapticsEnabled = hapticsEnabled,
                onToggleHaptics = { enabled ->
                  hapticsEnabled = enabled
                  JuddlesNotificationManager.saveHapticsEnabled(context, uid, enabled)
                  if (enabled) {
                    JuddlesNotificationManager.triggerHapticFeedback(context)
                  }
                  viewModel.setFeedbackMessage(
                    if (enabled) "Haptic vibration enabled" else "Haptic vibration disabled"
                  )
                },
                onUpdateProfile = { viewModel.updateProfile(it) },
                onOpenExportDialog = { fmt ->
                  showExportModalFormat = fmt ?: ExportFormat.PDF
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

    // Custom Date Reminder Modal
    if (showCustomReminderModal) {
      CustomDateReminderDialog(
        currencyCode = state.profile.preferredCurrency,
        onDismiss = { showCustomReminderModal = false },
        onSaveReminder = { title, category, targetDateText, targetTimeText, amountMinor, notifyPush ->
          val newRem = CustomDateReminder(
            id = "rem_${System.currentTimeMillis()}",
            title = title,
            category = category,
            targetDateText = targetDateText,
            targetTimeText = targetTimeText,
            amountMinor = amountMinor,
            notifyPush = notifyPush,
            isCompleted = false
          )
          val updated = listOf(newRem) + reminders
          reminders = updated
          JuddlesNotificationManager.saveReminders(context, uid, updated)

          val formattedAmt = FinancialCalculator.formatCurrencyMinor(amountMinor, state.profile.preferredCurrency)
          JuddlesNotificationManager.scheduleReminderAlarm(context, newRem, formattedAmt)

          if (notifyPush) {
            if (!JuddlesNotificationManager.hasNotificationPermission(context) &&
              Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
              notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
              JuddlesNotificationManager.sendInstantNotification(
                context = context,
                title = "Reminder Scheduled: $title",
                message = "$category set for $targetDateText at $targetTimeText",
                vibrate = hapticsEnabled
              )
            }
          }
          viewModel.setFeedbackMessage("Reminder scheduled for $targetDateText ($targetTimeText)")
        }
      )
    }

    // Export Report Dialog (.PDF, .CSV, .TXT, .MD)
    val activeExportFormat = showExportModalFormat
    if (activeExportFormat != null) {
      ExportReportDialog(
        initialFormat = activeExportFormat,
        periodLabel = state.selectedPeriod.label,
        onDismiss = { showExportModalFormat = null },
        onSaveToDevice = { format ->
          pendingSaveFormat = format
          val defaultName = ReportExportManager.defaultFileName(format, state)
          createDocumentLauncher.launch(defaultName)
        },
        onShareFile = { format ->
          val res = ReportExportManager.shareExportFile(context, format, state)
          viewModel.setFeedbackMessage(
            res.getOrElse { "Share error: ${it.localizedMessage}" }
          )
        }
      )
    }

    // More Financial Tools Modal (Quick Compact Mode, Currencies, 12 Themes, 8 Fonts, Reminders, Export, Transfer)
    if (showMoreToolsModal) {
      MoreToolsDialog(
        compactMode = state.profile.compactMode,
        currentThemeMode = state.profile.themeMode,
        currentFontMode = currentFontMode,
        currentCurrency = state.profile.preferredCurrency,
        onDismiss = { showMoreToolsModal = false },
        onToggleCompactMode = { viewModel.toggleCompactMode() },
        onSelectTheme = { viewModel.setThemeMode(it) },
        onSelectFont = { fontKey ->
          currentFontMode = fontKey
          JuddlesNotificationManager.saveFontPreference(context, uid, fontKey)
          viewModel.setFeedbackMessage("Font updated to $fontKey")
        },
        onSelectCurrency = { curr ->
          viewModel.updateProfile(
            updated = state.profile.copy(preferredCurrency = curr),
            feedbackText = "Currency updated to $curr"
          )
        },
        onOpenCustomReminderClick = { showCustomReminderModal = true },
        onOpenTransferClick = { showAddTxType = "TRANSFER" },
        onOpenExportClick = { fmt -> showExportModalFormat = fmt },
        onOpenReportsClick = { currentDestination = SignedInDestination.REPORTS }
      )
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

    // Alerts, Custom Date Reminders & Push Notifications Status Modal
    if (showNotificationsModal) {
      val activeBudget = state.budgets.firstOrNull()
      val spent = FinancialCalculator.totalExpensesMinor(
        FinancialCalculator.filterByPeriod(state.transactions, state.selectedPeriod)
      )
      val limit = activeBudget?.limitMinor ?: 200000L
      val pct = FinancialCalculator.budgetUsagePercent(limit, spent)
      val completedGoals = state.goals.count { it.currentMinor >= it.targetMinor || it.status == "COMPLETED" }
      val pendingReminders = reminders.filter { !it.isCompleted }

      AlertDialog(
        onDismissRequest = { showNotificationsModal = false },
        title = { Text("Juddles Smart Alerts & Reminders") },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              "• Budget status: $pct% of your ${
                FinancialCalculator.formatCurrencyMinor(limit, state.profile.preferredCurrency, includeCents = false)
              } limit used.\n" +
                "• Completed goals: $completedGoals of ${state.goals.size} goals reached.\n" +
                "• Upcoming custom reminders: ${pendingReminders.size} active."
            )
            pendingReminders.take(3).forEach { rem ->
              Text(
                text = "⏰ ${rem.title} — ${rem.targetDateText} (${rem.targetTimeText})",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        },
        confirmButton = {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = {
                showNotificationsModal = false
                showCustomReminderModal = true
              }
            ) {
              Text("+ Add Reminder")
            }
            Button(
              onClick = { showNotificationsModal = false },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Got it")
            }
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
