package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.model.SpendingBudget
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class JuddlesRepository(private val db: FirebaseFirestore) {

  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth = Firebase.auth

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
  }

  fun observeUserProfile(userId: String): Flow<UserProfile?> {
    val docRef = db.collection("users").document(userId)
    return docRef.snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        } else {
          null
        }
      }
      .catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
          .filterIsInstance<FirebaseFirestoreException>()
          .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == userId) {
          handleFirestoreError(exceptionToLog, OperationType.GET, docRef.path)
        }
        throw error
      }
  }

  fun observeTransactions(userId: String): Flow<List<FinancialTransaction>> {
    val path = "users/$userId/transactions"
    return db.collection("users").document(userId).collection("transactions")
      .whereEqualTo("userId", userId)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          FinancialTransaction::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).sortedByDescending { it.occurredAtEpochMillis }
      }
      .catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
          .filterIsInstance<FirebaseFirestoreException>()
          .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == userId) {
          handleFirestoreError(exceptionToLog, OperationType.LIST, path)
        }
        throw error
      }
  }

  fun observeGoals(userId: String): Flow<List<FinancialGoal>> {
    val path = "users/$userId/goals"
    return db.collection("users").document(userId).collection("goals")
      .whereEqualTo("userId", userId)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          FinancialGoal::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
      }
      .catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
          .filterIsInstance<FirebaseFirestoreException>()
          .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == userId) {
          handleFirestoreError(exceptionToLog, OperationType.LIST, path)
        }
        throw error
      }
  }

  fun observeBudgets(userId: String): Flow<List<SpendingBudget>> {
    val path = "users/$userId/budgets"
    return db.collection("users").document(userId).collection("budgets")
      .whereEqualTo("userId", userId)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          SpendingBudget::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        ).sortedByDescending { it.createdAt ?: Timestamp(0, 0) }
      }
      .catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
          .filterIsInstance<FirebaseFirestoreException>()
          .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == userId) {
          handleFirestoreError(exceptionToLog, OperationType.LIST, path)
        }
        throw error
      }
  }

  suspend fun ensureOrSaveUserProfile(profile: UserProfile): Result<Unit> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
    try {
      val existing = docRef.get().await()
      if (!existing.exists()) {
        val payload = mapOf(
          "userId" to uid,
          "displayName" to profile.displayName.take(100),
          "email" to profile.email.take(200),
          "preferredCurrency" to profile.preferredCurrency.ifBlank { "USD" }.take(10),
          "openingBalanceMinor" to profile.openingBalanceMinor,
          "monthlyGoalMinor" to profile.monthlyGoalMinor.coerceAtLeast(0L),
          "compactMode" to profile.compactMode,
          "hideAmounts" to profile.hideAmounts,
          "themeMode" to profile.themeMode.ifBlank { "LIGHT" },
          "notificationsEnabled" to profile.notificationsEnabled,
          "budgetAlertThreshold" to profile.budgetAlertThreshold.coerceIn(10L, 100L),
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      } else {
        val updatePayload = mapOf(
          "displayName" to profile.displayName.take(100),
          "email" to profile.email.take(200),
          "preferredCurrency" to profile.preferredCurrency.ifBlank { "USD" }.take(10),
          "openingBalanceMinor" to profile.openingBalanceMinor,
          "monthlyGoalMinor" to profile.monthlyGoalMinor.coerceAtLeast(0L),
          "compactMode" to profile.compactMode,
          "hideAmounts" to profile.hideAmounts,
          "themeMode" to profile.themeMode.ifBlank { "LIGHT" },
          "notificationsEnabled" to profile.notificationsEnabled,
          "budgetAlertThreshold" to profile.budgetAlertThreshold.coerceIn(10L, 100L),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.update(updatePayload).await()
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      throw e
    }
  }

  suspend fun createTransaction(transaction: FinancialTransaction): Result<String> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("transactions").document()
    val payload = mapOf(
      "userId" to uid,
      "type" to transaction.type,
      "amountMinor" to transaction.amountMinor.coerceAtLeast(1L),
      "currency" to transaction.currency.ifBlank { "USD" }.take(10),
      "category" to transaction.category.ifBlank { "General" }.take(60),
      "accountName" to transaction.accountName.ifBlank { "Main Wallet" }.take(60),
      "targetAccountName" to transaction.targetAccountName.take(60),
      "note" to transaction.note.take(300),
      "merchant" to transaction.merchant.take(100),
      "recurring" to transaction.recurring,
      "frequency" to transaction.frequency.ifBlank { "NONE" },
      "occurredAtEpochMillis" to transaction.occurredAtEpochMillis.coerceAtLeast(1L),
      "goalId" to transaction.goalId.take(128),
      "createdAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun updateTransaction(transaction: FinancialTransaction): Result<Unit> = runCatching {
    val uid = requireUserId()
    require(transaction.id.isNotBlank()) { "Transaction ID must not be blank" }
    val docRef = db.collection("users").document(uid).collection("transactions").document(transaction.id)
    val updatePayload = mapOf(
      "type" to transaction.type,
      "amountMinor" to transaction.amountMinor.coerceAtLeast(1L),
      "currency" to transaction.currency.ifBlank { "USD" }.take(10),
      "category" to transaction.category.ifBlank { "General" }.take(60),
      "accountName" to transaction.accountName.ifBlank { "Main Wallet" }.take(60),
      "targetAccountName" to transaction.targetAccountName.take(60),
      "note" to transaction.note.take(300),
      "merchant" to transaction.merchant.take(100),
      "recurring" to transaction.recurring,
      "frequency" to transaction.frequency.ifBlank { "NONE" },
      "occurredAtEpochMillis" to transaction.occurredAtEpochMillis.coerceAtLeast(1L),
      "goalId" to transaction.goalId.take(128),
      "updatedAt" to FieldValue.serverTimestamp()
    )
    try {
      docRef.update(updatePayload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  suspend fun deleteTransaction(transactionId: String): Result<Unit> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("transactions").document(transactionId)
    try {
      docRef.delete().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      throw e
    }
  }

  suspend fun getTransactionById(ownerUserId: String, transactionId: String): Result<FinancialTransaction?> = runCatching {
    val docRef = db.collection("users").document(ownerUserId).collection("transactions").document(transactionId)
    try {
      val snapshot = docRef.get().await()
      snapshot.toObject(FinancialTransaction::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.GET, docRef.path)
      throw e
    }
  }

  suspend fun createGoal(goal: FinancialGoal): Result<String> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("goals").document()
    val status = if (goal.currentMinor >= goal.targetMinor) "COMPLETED" else goal.status.ifBlank { "ACTIVE" }
    val payload = mapOf(
      "userId" to uid,
      "title" to goal.title.ifBlank { "My Goal" }.take(100),
      "targetMinor" to goal.targetMinor.coerceAtLeast(1L),
      "currentMinor" to goal.currentMinor.coerceAtLeast(0L),
      "currency" to goal.currency.ifBlank { "USD" }.take(10),
      "targetDate" to goal.targetDate.take(40),
      "status" to status,
      "iconKey" to goal.iconKey.ifBlank { "TRAVEL" }.take(40),
      "notes" to goal.notes.take(300),
      "createdAt" to FieldValue.serverTimestamp(),
      "updatedAt" to FieldValue.serverTimestamp()
    )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun updateGoal(goal: FinancialGoal): Result<Unit> = runCatching {
    val uid = requireUserId()
    require(goal.id.isNotBlank()) { "Goal ID must not be blank" }
    val docRef = db.collection("users").document(uid).collection("goals").document(goal.id)
    val updatePayload = mapOf(
      "title" to goal.title.ifBlank { "My Goal" }.take(100),
      "targetMinor" to goal.targetMinor.coerceAtLeast(1L),
      "currentMinor" to goal.currentMinor.coerceAtLeast(0L),
      "currency" to goal.currency.ifBlank { "USD" }.take(10),
      "targetDate" to goal.targetDate.take(40),
      "status" to goal.status.ifBlank { "ACTIVE" },
      "iconKey" to goal.iconKey.ifBlank { "TRAVEL" }.take(40),
      "notes" to goal.notes.take(300),
      "updatedAt" to FieldValue.serverTimestamp()
    )
    try {
      docRef.update(updatePayload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  /**
   * Adds a contribution to a goal AND records a linked TRANSFER transaction so that
   * goal progress updates while avoiding double-counting in income/expenses (per spec Section 3 & 4).
   */
  suspend fun contributeToGoal(
    goal: FinancialGoal,
    contributionMinor: Long,
    recordLinkedTransfer: Boolean = true
  ): Result<Unit> = runCatching {
    val uid = requireUserId()
    val newCurrent = (goal.currentMinor + contributionMinor).coerceAtLeast(0L)
    val newStatus = if (newCurrent >= goal.targetMinor) "COMPLETED" else "ACTIVE"
    updateGoal(
      goal.copy(
        currentMinor = newCurrent,
        status = newStatus
      )
    ).getOrThrow()

    if (recordLinkedTransfer && contributionMinor > 0L) {
      createTransaction(
        FinancialTransaction(
          userId = uid,
          type = "TRANSFER",
          amountMinor = contributionMinor,
          currency = goal.currency,
          category = "Goal Contribution",
          accountName = "Main Wallet",
          targetAccountName = goal.title.take(60),
          note = "Contribution toward ${goal.title}",
          merchant = "",
          recurring = false,
          frequency = "NONE",
          occurredAtEpochMillis = System.currentTimeMillis(),
          goalId = goal.id
        )
      ).getOrThrow()
    }
  }

  suspend fun deleteGoal(goalId: String): Result<Unit> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("goals").document(goalId)
    try {
      docRef.delete().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      throw e
    }
  }

  suspend fun saveBudget(budget: SpendingBudget): Result<String> = runCatching {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("budgets")
    val docRef = if (budget.id.isNotBlank()) collectionRef.document(budget.id) else collectionRef.document()
    try {
      val existing = if (budget.id.isNotBlank()) docRef.get().await().exists() else false
      if (!existing) {
        val payload = mapOf(
          "userId" to uid,
          "name" to budget.name.ifBlank { "Monthly Budget" }.take(100),
          "periodType" to budget.periodType.ifBlank { "MONTHLY" },
          "category" to budget.category.ifBlank { "ALL" }.take(60),
          "currency" to budget.currency.ifBlank { "USD" }.take(10),
          "limitMinor" to budget.limitMinor.coerceAtLeast(1L),
          "rolloverEnabled" to budget.rolloverEnabled,
          "alertThresholdPercent" to budget.alertThresholdPercent.coerceIn(10L, 100L),
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      } else {
        val updatePayload = mapOf(
          "name" to budget.name.ifBlank { "Monthly Budget" }.take(100),
          "periodType" to budget.periodType.ifBlank { "MONTHLY" },
          "category" to budget.category.ifBlank { "ALL" }.take(60),
          "currency" to budget.currency.ifBlank { "USD" }.take(10),
          "limitMinor" to budget.limitMinor.coerceAtLeast(1L),
          "rolloverEnabled" to budget.rolloverEnabled,
          "alertThresholdPercent" to budget.alertThresholdPercent.coerceIn(10L, 100L),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.update(updatePayload).await()
      }
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      throw e
    }
  }

  suspend fun deleteBudget(budgetId: String): Result<Unit> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("budgets").document(budgetId)
    try {
      docRef.delete().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      throw e
    }
  }

  suspend fun clearAllUserData(): Result<Unit> = runCatching {
    val uid = requireUserId()
    val userDoc = db.collection("users").document(uid)
    try {
      val txDocs = userDoc.collection("transactions").whereEqualTo("userId", uid).get().await()
      for (doc in txDocs.documents) {
        doc.reference.delete().await()
      }
      val goalDocs = userDoc.collection("goals").whereEqualTo("userId", uid).get().await()
      for (doc in goalDocs.documents) {
        doc.reference.delete().await()
      }
      val budgetDocs = userDoc.collection("budgets").whereEqualTo("userId", uid).get().await()
      for (doc in budgetDocs.documents) {
        doc.reference.delete().await()
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, userDoc.path)
      throw e
    }
  }
}
