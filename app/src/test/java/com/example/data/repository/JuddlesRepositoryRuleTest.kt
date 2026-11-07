package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.model.SpendingBudget
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JuddlesRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun createAndObserveTransactions_authenticatedOwner_succeeds() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = JuddlesRepository(firestore)

    val tx = FinancialTransaction(
      userId = uid,
      type = "EXPENSE",
      amountMinor = 2450L,
      currency = "USD",
      category = "Food",
      accountName = "Main Wallet",
      note = "Lunch",
      merchant = "Cafe",
      occurredAtEpochMillis = 1730940000000L
    )

    val createResult = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createTransaction(tx) }
    assertTrue("Expected transaction creation to succeed", createResult.isSuccess)
    val txId = createResult.getOrThrow()

    val emitted = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeTransactions(uid).first { list -> list.any { it.id == txId } }
    }
    assertTrue(emitted.any { it.id == txId && it.amountMinor == 2450L })
  }

  @Test
  fun createGoalAndBudget_authenticatedOwner_succeeds() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = JuddlesRepository(firestore)

    val goal = FinancialGoal(
      userId = uid,
      title = "Travel the World",
      targetMinor = 300000L,
      currentMinor = 150000L,
      currency = "USD",
      targetDate = "2027-06-01",
      iconKey = "TRAVEL"
    )
    val goalId = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createGoal(goal).getOrThrow() }
    assertTrue(goalId.isNotEmpty())

    val budget = SpendingBudget(
      userId = uid,
      name = "Monthly Budget",
      periodType = "MONTHLY",
      category = "ALL",
      currency = "USD",
      limitMinor = 200000L
    )
    val budgetId = withTimeout(DEFAULT_TIMEOUT_MS) { repository.saveBudget(budget).getOrThrow() }
    assertTrue(budgetId.isNotEmpty())
  }

  @Test
  fun getTransactionById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val aliceRepo = JuddlesRepository(firestore)
    val txId = withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceRepo.createTransaction(
        FinancialTransaction(
          userId = aliceUid,
          type = "EXPENSE",
          amountMinor = 1200L,
          currency = "USD",
          category = "Transport",
          accountName = "Main Wallet",
          occurredAtEpochMillis = 1730940000000L
        )
      ).getOrThrow()
    }

    signInTestUser(BOB_EMAIL)
    val bobRepo = JuddlesRepository(firestore)
    val result = withTimeout(DEFAULT_TIMEOUT_MS) {
      bobRepo.getTransactionById(aliceUid, txId)
    }
    assertTrue("Expected cross-user read to fail", result.isFailure)
    val firestoreEx = result.exceptionOrNull()?.unwrapFirestoreException()
    assertNotNull(firestoreEx)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
  }

  @Test
  fun observeTransactions_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    auth.signOut()
    val repository = JuddlesRepository(firestore)

    var caught: Throwable? = null
    try {
      withTimeout(FLOW_TIMEOUT_MS) {
        repository.observeTransactions(aliceUid).first()
      }
    } catch (e: Throwable) {
      caught = e
    }
    assertNotNull("Expected exception for unauthenticated query", caught)
    val firestoreEx = caught?.unwrapFirestoreException()
    assertNotNull("Expected nested FirebaseFirestoreException, got $caught", firestoreEx)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
  }

  @Test
  fun financialCalculator_reconcilesBalancesTransfersAndGoals() {
    val txs = listOf(
      FinancialTransaction(type = "INCOME", amountMinor = 500000L, category = "Salary"),
      FinancialTransaction(type = "EXPENSE", amountMinor = 125000L, category = "Food"),
      FinancialTransaction(type = "TRANSFER", amountMinor = 50000L, category = "Goal Contribution")
    )
    assertEquals(500000L, FinancialCalculator.totalIncomeMinor(txs))
    assertEquals(125000L, FinancialCalculator.totalExpensesMinor(txs))
    assertEquals(375000L, FinancialCalculator.netCashFlowMinor(txs))
    assertEquals(475000L, FinancialCalculator.currentBalanceMinor(100000L, txs))
    assertEquals(100, FinancialCalculator.goalProgressPercent(350000L, 300000L))
    assertEquals(75, FinancialCalculator.budgetUsagePercent(200000L, 150000L))
    assertEquals(50000L, FinancialCalculator.budgetRemainingMinor(200000L, 150000L))
  }

  private companion object {
    const val ALICE_EMAIL = "alice@juddles.test"
    const val BOB_EMAIL = "bob@juddles.test"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}
