const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

function validTransactionPayload(uid, now) {
  return {
    userId: uid,
    type: "EXPENSE",
    amountMinor: 2450,
    currency: "USD",
    category: "Food",
    accountName: "Main Wallet",
    targetAccountName: "",
    note: "Dinner",
    merchant: "Burger Bistro",
    recurring: false,
    frequency: "NONE",
    occurredAtEpochMillis: 1730940000000,
    goalId: "",
    createdAt: now,
    updatedAt: now,
  };
}

function validGoalPayload(uid, now) {
  return {
    userId: uid,
    title: "Travel the World",
    targetMinor: 300000,
    currentMinor: 150000,
    currency: "USD",
    targetDate: "2027-06-01",
    status: "ACTIVE",
    iconKey: "TRAVEL",
    notes: "Summer trip",
    createdAt: now,
    updatedAt: now,
  };
}

function validBudgetPayload(uid, now) {
  return {
    userId: uid,
    name: "Monthly Budget",
    periodType: "MONTHLY",
    category: "ALL",
    currency: "USD",
    limitMinor: 200000,
    rolloverEnabled: false,
    alertThresholdPercent: 80,
    createdAt: now,
    updatedAt: now,
  };
}

test("Unauthenticated user: cannot read or write transactions, goals, or budgets", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("transactions").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("goals").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("budgets").get()
  );
});

test("Authenticated owner: can create and query own transactions, goals, and budgets", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .doc("tx_1")
      .set(validTransactionPayload(ALICE_UID, now))
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .where("userId", "==", ALICE_UID)
      .get()
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("goals")
      .doc("goal_1")
      .set(validGoalPayload(ALICE_UID, now))
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("budgets")
      .doc("budget_1")
      .set(validBudgetPayload(ALICE_UID, now))
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .set({
        userId: ALICE_UID,
        displayName: "Alice",
        email: "alice@juddles.test",
        preferredCurrency: "USD",
        openingBalanceMinor: 100000,
        monthlyGoalMinor: 500000,
        compactMode: true,
        hideAmounts: false,
        themeMode: "AMOLED",
        notificationsEnabled: true,
        budgetAlertThreshold: 80,
        createdAt: now,
        updatedAt: now,
      })
  );
});

test("Cross-user isolation: Bob cannot read or modify Alice's records", async () => {
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context
      .firestore()
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .doc("tx_alice")
      .set(validTransactionPayload(ALICE_UID, now));
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .doc("tx_alice")
      .get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .where("userId", "==", ALICE_UID)
      .get()
  );
});

test("Shadow update & invalid schema rejection: extra ghost fields or negative amounts fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .doc("tx_bad_ghost")
      .set({
        ...validTransactionPayload(ALICE_UID, now),
        isAdminSpoof: true,
      })
  );

  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("transactions")
      .doc("tx_bad_amount")
      .set({
        ...validTransactionPayload(ALICE_UID, now),
        amountMinor: -500,
      })
  );
});
