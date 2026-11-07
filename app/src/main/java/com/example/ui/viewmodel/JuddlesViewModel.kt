package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.model.SpendingBudget
import com.example.data.model.TimePeriodFilter
import com.example.data.model.UserProfile
import com.example.data.repository.JuddlesRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DataLoadState<out T> {
  data object Loading : DataLoadState<Nothing>
  data class Success<T>(val data: T) : DataLoadState<T>
  data class Error(val message: String) : DataLoadState<Nothing>
}

data class JuddlesDashboardState(
  val isLoading: Boolean = true,
  val errorMessage: String? = null,
  val actionFeedback: String? = null,
  val isSampleWorkspaceLoaded: Boolean = false,
  val profile: UserProfile = UserProfile(),
  val transactions: List<FinancialTransaction> = emptyList(),
  val goals: List<FinancialGoal> = emptyList(),
  val budgets: List<SpendingBudget> = emptyList(),
  val selectedPeriod: TimePeriodFilter = TimePeriodFilter.THIS_MONTH,
  val selectedReportsTab: Int = 0, // 0 = Overview, 1 = Trends, 2 = Insights
  val searchQuery: String = "",
  val selectedCategoryFilter: String = "All"
)

class JuddlesViewModel(
  private val repository: JuddlesRepository,
  private val currentUserId: String,
  private val initialDisplayName: String = "Explorer",
  private val initialEmail: String = ""
) : ViewModel() {

  private val _selectedPeriod = MutableStateFlow(TimePeriodFilter.THIS_MONTH)
  private val _selectedReportsTab = MutableStateFlow(0)
  private val _searchQuery = MutableStateFlow("")
  private val _selectedCategoryFilter = MutableStateFlow("All")
  private val _actionFeedback = MutableStateFlow<String?>(null)
  private val _errorMessage = MutableStateFlow<String?>(null)

  val selectedPeriod: StateFlow<TimePeriodFilter> = _selectedPeriod.asStateFlow()
  val selectedReportsTab: StateFlow<Int> = _selectedReportsTab.asStateFlow()
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
  val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

  private val profileFlow = repository.observeUserProfile(currentUserId)
    .catch { e ->
      handleStreamError("profile", e)
      emit(null)
    }

  private val transactionsFlow = repository.observeTransactions(currentUserId)
    .catch { e ->
      handleStreamError("transactions", e)
      emit(emptyList())
    }

  private val goalsFlow = repository.observeGoals(currentUserId)
    .catch { e ->
      handleStreamError("goals", e)
      emit(emptyList())
    }

  private val budgetsFlow = repository.observeBudgets(currentUserId)
    .catch { e ->
      handleStreamError("budgets", e)
      emit(emptyList())
    }

  val uiState: StateFlow<JuddlesDashboardState> = combine(
    profileFlow,
    transactionsFlow,
    goalsFlow,
    budgetsFlow,
    combine(_selectedPeriod, _selectedReportsTab, _searchQuery, _selectedCategoryFilter, _actionFeedback) { period, repTab, query, catFilter, feedback ->
      FilterBundle(period, repTab, query, catFilter, feedback)
    }
  ) { profile, transactions, goals, budgets, filterBundle ->
    val resolvedProfile = profile ?: UserProfile(
      userId = currentUserId,
      displayName = initialDisplayName.ifBlank { "Explorer" },
      email = initialEmail
    )
    val hasSampleTag = transactions.any { it.note.contains("[Sample]") } ||
      goals.any { it.notes.contains("[Sample]") }

    JuddlesDashboardState(
      isLoading = false,
      errorMessage = _errorMessage.value,
      actionFeedback = filterBundle.feedback,
      isSampleWorkspaceLoaded = hasSampleTag,
      profile = resolvedProfile,
      transactions = transactions,
      goals = goals,
      budgets = budgets,
      selectedPeriod = filterBundle.period,
      selectedReportsTab = filterBundle.repTab,
      searchQuery = filterBundle.query,
      selectedCategoryFilter = filterBundle.catFilter
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000L),
    initialValue = JuddlesDashboardState(
      isLoading = true,
      profile = UserProfile(
        userId = currentUserId,
        displayName = initialDisplayName.ifBlank { "Explorer" },
        email = initialEmail
      )
    )
  )

  private data class FilterBundle(
    val period: TimePeriodFilter,
    val repTab: Int,
    val query: String,
    val catFilter: String,
    val feedback: String?
  )

  init {
    viewModelScope.launch {
      // Ensure user profile document exists in Firestore upon authentication
      val current = uiState.value.profile
      repository.ensureOrSaveUserProfile(
        current.copy(
          userId = currentUserId,
          displayName = initialDisplayName.ifBlank { "Explorer" },
          email = initialEmail
        )
      )
    }
  }

  private fun handleStreamError(source: String, error: Throwable) {
    val firestoreError = generateSequence(error) { it.cause }
      .filterIsInstance<FirebaseFirestoreException>()
      .firstOrNull()
    if (firestoreError == null && error is CancellationException) throw error
    val rootError = firestoreError ?: error
    Log.w("JuddlesVM", "Error observing $source", rootError)
    _errorMessage.value = rootError.localizedMessage ?: "Failed to load $source"
  }

  fun clearFeedback() {
    _actionFeedback.value = null
    _errorMessage.value = null
  }

  fun setPeriod(period: TimePeriodFilter) {
    _selectedPeriod.value = period
  }

  fun setReportsTab(index: Int) {
    _selectedReportsTab.value = index
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategoryFilter(category: String) {
    _selectedCategoryFilter.value = category
  }

  fun toggleHideAmounts() {
    val current = uiState.value.profile
    updateProfile(current.copy(hideAmounts = !current.hideAmounts))
  }

  fun updateProfile(updated: UserProfile) {
    viewModelScope.launch {
      val result = repository.ensureOrSaveUserProfile(updated)
      if (result.isSuccess) {
        _actionFeedback.value = "Preferences saved"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not save preferences"
      }
    }
  }

  fun saveTransaction(
    existingId: String = "",
    type: String,
    amountMinor: Long,
    category: String,
    accountName: String = "Main Wallet",
    targetAccountName: String = "",
    note: String = "",
    merchant: String = "",
    recurring: Boolean = false,
    frequency: String = "NONE",
    occurredAtEpochMillis: Long = System.currentTimeMillis()
  ) {
    viewModelScope.launch {
      val currency = uiState.value.profile.preferredCurrency
      val tx = FinancialTransaction(
        id = existingId,
        userId = currentUserId,
        type = type,
        amountMinor = amountMinor,
        currency = currency,
        category = category,
        accountName = accountName,
        targetAccountName = targetAccountName,
        note = note,
        merchant = merchant,
        recurring = recurring,
        frequency = if (recurring) frequency.ifBlank { "MONTHLY" } else "NONE",
        occurredAtEpochMillis = occurredAtEpochMillis
      )
      val result = if (existingId.isBlank()) {
        repository.createTransaction(tx)
      } else {
        repository.updateTransaction(tx).map { existingId }
      }
      if (result.isSuccess) {
        _actionFeedback.value = if (existingId.isBlank()) "Transaction saved" else "Transaction updated"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not save transaction"
      }
    }
  }

  fun deleteTransaction(transactionId: String) {
    viewModelScope.launch {
      val result = repository.deleteTransaction(transactionId)
      if (result.isSuccess) {
        _actionFeedback.value = "Transaction deleted"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not delete transaction"
      }
    }
  }

  fun saveGoal(
    existingId: String = "",
    title: String,
    targetMinor: Long,
    currentMinor: Long,
    targetDate: String,
    iconKey: String,
    status: String = "ACTIVE",
    notes: String = ""
  ) {
    viewModelScope.launch {
      val currency = uiState.value.profile.preferredCurrency
      val resolvedStatus = if (currentMinor >= targetMinor) "COMPLETED" else status
      val goal = FinancialGoal(
        id = existingId,
        userId = currentUserId,
        title = title,
        targetMinor = targetMinor,
        currentMinor = currentMinor,
        currency = currency,
        targetDate = targetDate,
        status = resolvedStatus,
        iconKey = iconKey,
        notes = notes
      )
      val result = if (existingId.isBlank()) {
        repository.createGoal(goal)
      } else {
        repository.updateGoal(goal).map { existingId }
      }
      if (result.isSuccess) {
        _actionFeedback.value = if (existingId.isBlank()) "Goal created" else "Goal updated"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not save goal"
      }
    }
  }

  fun contributeToGoal(goal: FinancialGoal, contributionMinor: Long) {
    viewModelScope.launch {
      val result = repository.contributeToGoal(goal, contributionMinor, recordLinkedTransfer = true)
      if (result.isSuccess) {
        _actionFeedback.value = "Contribution added to ${goal.title}"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not add contribution"
      }
    }
  }

  fun deleteGoal(goalId: String) {
    viewModelScope.launch {
      val result = repository.deleteGoal(goalId)
      if (result.isSuccess) {
        _actionFeedback.value = "Goal removed"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not delete goal"
      }
    }
  }

  fun saveBudget(
    existingId: String = "",
    name: String,
    periodType: String = "MONTHLY",
    category: String = "ALL",
    limitMinor: Long,
    rolloverEnabled: Boolean = false,
    alertThresholdPercent: Long = 80L
  ) {
    viewModelScope.launch {
      val currency = uiState.value.profile.preferredCurrency
      val budget = SpendingBudget(
        id = existingId,
        userId = currentUserId,
        name = name,
        periodType = periodType,
        category = category,
        currency = currency,
        limitMinor = limitMinor,
        rolloverEnabled = rolloverEnabled,
        alertThresholdPercent = alertThresholdPercent
      )
      val result = repository.saveBudget(budget)
      if (result.isSuccess) {
        _actionFeedback.value = "Budget saved"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not save budget"
      }
    }
  }

  fun deleteBudget(budgetId: String) {
    viewModelScope.launch {
      val result = repository.deleteBudget(budgetId)
      if (result.isSuccess) {
        _actionFeedback.value = "Budget removed"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Could not delete budget"
      }
    }
  }

  /**
   * Explicitly user-triggered Sample Workspace matching the reference design numbers
   * (clearly labelled with [Sample] and removable at any time per PDF Section 8).
   */
  fun loadSampleWorkspace() {
    viewModelScope.launch {
      val now = System.currentTimeMillis()
      val dayMs = 86_400_000L
      val currency = uiState.value.profile.preferredCurrency

      // Update opening balance so Total Balance = $4,820.75 with the sample income & expenses
      // Income = $2,570.00 + $3,500.75 = $6,070.75; Expenses = $420 + $250 + $300 + $280 = $1,250.00
      // Total Balance = $0 + $6,070.75 - $1,250.00 = $4,820.75!
      repository.ensureOrSaveUserProfile(
        uiState.value.profile.copy(
          openingBalanceMinor = 0L,
          monthlyGoalMinor = 500000L // $5,000.00
        )
      )

      // Sample Income & Expenses matching the Expenses donut ($1,250 Total Spent: Food $420, Transport $250, Shopping $300, Bills $280)
      val sampleTransactions = listOf(
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 2450L,
          currency = currency,
          category = "Food",
          accountName = "Main Wallet",
          note = "Burger & Fries [Sample]",
          merchant = "Green Kitchen",
          occurredAtEpochMillis = now - 3_600_000L
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 1200L,
          currency = currency,
          category = "Transport",
          accountName = "Main Wallet",
          note = "City Ride [Sample]",
          merchant = "Metro Cab",
          occurredAtEpochMillis = now - 7_200_000L
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 8999L,
          currency = currency,
          category = "Shopping",
          accountName = "Main Wallet",
          note = "Autumn Essentials [Sample]",
          merchant = "Urban Market",
          occurredAtEpochMillis = now - 2 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 39550L,
          currency = currency,
          category = "Food",
          accountName = "Main Wallet",
          note = "Monthly Groceries [Sample]",
          merchant = "Fresh Mart",
          occurredAtEpochMillis = now - 3 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 23800L,
          currency = currency,
          category = "Transport",
          accountName = "Main Wallet",
          note = "Transit Pass & Fuel [Sample]",
          merchant = "City Transit",
          occurredAtEpochMillis = now - 4 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 21001L,
          currency = currency,
          category = "Shopping",
          accountName = "Main Wallet",
          note = "Home Office Gear [Sample]",
          merchant = "Studio Supply",
          occurredAtEpochMillis = now - 5 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 28000L,
          currency = currency,
          category = "Bills",
          accountName = "Main Wallet",
          note = "Utilities & Internet [Sample]",
          merchant = "Grid Power",
          occurredAtEpochMillis = now - 6 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "INCOME",
          amountMinor = 257000L,
          currency = currency,
          category = "Salary",
          accountName = "Main Wallet",
          note = "Monthly Salary [Sample]",
          merchant = "Acme Studio",
          occurredAtEpochMillis = now - 5 * dayMs
        ),
        // Prior month entries for multi-month Reports comparison
        FinancialTransaction(
          userId = currentUserId,
          type = "INCOME",
          amountMinor = 220000L,
          currency = currency,
          category = "Salary",
          accountName = "Main Wallet",
          note = "October Salary [Sample]",
          merchant = "Acme Studio",
          occurredAtEpochMillis = now - 32 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 110000L,
          currency = currency,
          category = "Bills",
          accountName = "Main Wallet",
          note = "October Living Expenses [Sample]",
          merchant = "Various",
          occurredAtEpochMillis = now - 31 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "INCOME",
          amountMinor = 130075L,
          currency = currency,
          category = "Freelance",
          accountName = "Main Wallet",
          note = "September Design Project [Sample]",
          merchant = "Client Co",
          occurredAtEpochMillis = now - 62 * dayMs
        ),
        FinancialTransaction(
          userId = currentUserId,
          type = "EXPENSE",
          amountMinor = 90000L,
          currency = currency,
          category = "Food",
          accountName = "Main Wallet",
          note = "September Expenses [Sample]",
          merchant = "Various",
          occurredAtEpochMillis = now - 60 * dayMs
        )
      )

      for (tx in sampleTransactions) {
        repository.createTransaction(tx)
      }

      // Sample Goals matching Screen 3 mockup:
      // 1. Travel the World ($3,000 / $3,000, COMPLETED)
      // 2. Buy a House ($10,000 / $20,000, 50%)
      // 3. New Car ($5,000 / $15,000, 33%)
      val sampleGoals = listOf(
        FinancialGoal(
          userId = currentUserId,
          title = "Travel the World",
          targetMinor = 300000L,
          currentMinor = 300000L,
          currency = currency,
          targetDate = "Dec 2026",
          status = "COMPLETED",
          iconKey = "TRAVEL",
          notes = "[Sample] Dream adventure fund"
        ),
        FinancialGoal(
          userId = currentUserId,
          title = "Buy a House",
          targetMinor = 2000000L,
          currentMinor = 1000000L,
          currency = currency,
          targetDate = "Jun 2028",
          status = "ACTIVE",
          iconKey = "HOME",
          notes = "[Sample] Down payment savings"
        ),
        FinancialGoal(
          userId = currentUserId,
          title = "New Car",
          targetMinor = 1500000L,
          currentMinor = 500000L,
          currency = currency,
          targetDate = "Sep 2027",
          status = "ACTIVE",
          iconKey = "CAR",
          notes = "[Sample] Electric vehicle fund"
        )
      )

      for (g in sampleGoals) {
        repository.createGoal(g)
      }

      // Sample Budget matching Screen 4 ($2,000 limit)
      repository.saveBudget(
        SpendingBudget(
          userId = currentUserId,
          name = "Monthly Budget",
          periodType = "MONTHLY",
          category = "ALL",
          currency = currency,
          limitMinor = 200000L,
          rolloverEnabled = false,
          alertThresholdPercent = 80L
        )
      )

      _actionFeedback.value = "Sample workspace loaded (removable anytime)"
    }
  }

  fun clearAllWorkspaceData() {
    viewModelScope.launch {
      val result = repository.clearAllUserData()
      repository.ensureOrSaveUserProfile(
        uiState.value.profile.copy(openingBalanceMinor = 0L)
      )
      if (result.isSuccess) {
        _actionFeedback.value = "All workspace records cleared"
      } else {
        _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Failed to clear workspace"
      }
    }
  }

  fun buildExportCsvSummary(): String {
    val state = uiState.value
    val currency = state.profile.preferredCurrency
    val periodTx = FinancialCalculator.filterByPeriod(state.transactions, state.selectedPeriod)
    val income = FinancialCalculator.totalIncomeMinor(periodTx)
    val expenses = FinancialCalculator.totalExpensesMinor(periodTx)
    val net = FinancialCalculator.netCashFlowMinor(periodTx)
    val balance = FinancialCalculator.currentBalanceMinor(state.profile.openingBalanceMinor, state.transactions)

    val sb = StringBuilder()
    sb.appendLine("Juddles Financial Report (${state.selectedPeriod.label})")
    sb.appendLine("Total Balance,${FinancialCalculator.formatCurrencyMinor(balance, currency)}")
    sb.appendLine("Period Income,${FinancialCalculator.formatCurrencyMinor(income, currency)}")
    sb.appendLine("Period Expenses,${FinancialCalculator.formatCurrencyMinor(expenses, currency)}")
    sb.appendLine("Net Cash Flow (Surplus),${FinancialCalculator.formatCurrencyMinor(net, currency)}")
    sb.appendLine()
    sb.appendLine("Type,Category,Amount,Account,Merchant,Note,Date")
    for (tx in periodTx) {
      val amt = FinancialCalculator.formatCurrencyMinor(tx.amountMinor, tx.currency)
      val date = FinancialCalculator.formatRelativeDate(tx.occurredAtEpochMillis).replace(",", " ")
      sb.appendLine("${tx.type},${tx.category},$amt,${tx.accountName},${tx.merchant},${tx.note},$date")
    }
    return sb.toString()
  }
}
