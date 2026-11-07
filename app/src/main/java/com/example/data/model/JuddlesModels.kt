package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@IgnoreExtraProperties
data class UserProfile(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "Explorer",
  val email: String = "",
  val preferredCurrency: String = "USD",
  val openingBalanceMinor: Long = 0L,
  val monthlyGoalMinor: Long = 500000L, // $5,000.00 default target
  val compactMode: Boolean = false,
  val hideAmounts: Boolean = false,
  val themeMode: String = "LIGHT", // LIGHT, DARK, OCEAN, SUNSET, LAVENDER, MIDNIGHT, SYSTEM
  val notificationsEnabled: Boolean = true,
  val budgetAlertThreshold: Long = 80L,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class FinancialTransaction(
  @DocumentId val id: String = "",
  val userId: String = "",
  val type: String = "EXPENSE", // INCOME, EXPENSE, TRANSFER
  val amountMinor: Long = 0L,
  val currency: String = "USD",
  val category: String = "Food",
  val accountName: String = "Main Wallet",
  val targetAccountName: String = "",
  val note: String = "",
  val merchant: String = "",
  val recurring: Boolean = false,
  val frequency: String = "NONE", // NONE, WEEKLY, MONTHLY, YEARLY
  val occurredAtEpochMillis: Long = 0L,
  val goalId: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class FinancialGoal(
  @DocumentId val id: String = "",
  val userId: String = "",
  val title: String = "",
  val targetMinor: Long = 100000L,
  val currentMinor: Long = 0L,
  val currency: String = "USD",
  val targetDate: String = "",
  val status: String = "ACTIVE", // ACTIVE, COMPLETED, PAUSED, ARCHIVED
  val iconKey: String = "TRAVEL", // TRAVEL, HOME, CAR, EMERGENCY, EDUCATION, TROPHY, TECH, HEALTH, WEDDING, RETIREMENT
  val notes: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class SpendingBudget(
  @DocumentId val id: String = "",
  val userId: String = "",
  val name: String = "Monthly Budget",
  val periodType: String = "MONTHLY", // WEEKLY, MONTHLY, YEARLY
  val category: String = "ALL", // ALL or specific category name
  val currency: String = "USD",
  val limitMinor: Long = 200000L, // $2,000.00 default
  val rolloverEnabled: Boolean = false,
  val alertThresholdPercent: Long = 80L,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

object JuddlesCategoryCatalog {
  val expenseCategories = listOf(
    "Food",
    "Groceries",
    "Dining Out",
    "Transport",
    "Fuel & EV",
    "Shopping",
    "Bills",
    "Rent & Mortgage",
    "Health & Fitness",
    "Entertainment",
    "Subscriptions",
    "Travel",
    "Education",
    "Personal Care",
    "Pets",
    "Gifts & Charity"
  )

  val incomeCategories = listOf(
    "Salary",
    "Freelance",
    "Business",
    "Investment",
    "Dividends",
    "Rental Income",
    "Bonus",
    "Refund",
    "Gift",
    "Other Income"
  )

  val goalIconKeys = listOf(
    "TRAVEL",
    "HOME",
    "CAR",
    "EMERGENCY",
    "EDUCATION",
    "TROPHY",
    "TECH",
    "HEALTH",
    "WEDDING",
    "RETIREMENT"
  )
}

enum class TimePeriodFilter(val label: String) {
  THIS_MONTH("This Month"),
  THIS_YEAR("This Year"),
  ALL_TIME("All Time")
}

data class CategoryBreakdownItem(
  val category: String,
  val amountMinor: Long,
  val percentage: Float,
)

data class MonthlyComparisonPoint(
  val monthLabel: String,
  val incomeMinor: Long,
  val expenseMinor: Long,
  val balanceEndMinor: Long,
)

data class JuddlesCurrencyOption(
  val code: String,
  val symbol: String,
  val name: String,
  val country: String,
  val flag: String,
  val region: String
)

object JuddlesCurrencyCatalog {
  val regions = listOf(
    "All",
    "South Asia & Nepal",
    "East & SE Asia",
    "Middle East & Central Asia",
    "Americas, Europe & Global"
  )

  val currencies: List<JuddlesCurrencyOption> = listOf(
    // South Asia & Himalayan (Featuring Nepal & Neighbors)
    JuddlesCurrencyOption("NPR", "रू", "Nepalese Rupee", "Nepal", "🇳🇵", "South Asia & Nepal"),
    JuddlesCurrencyOption("INR", "₹", "Indian Rupee", "India", "🇮🇳", "South Asia & Nepal"),
    JuddlesCurrencyOption("BDT", "৳", "Bangladeshi Taka", "Bangladesh", "🇧🇩", "South Asia & Nepal"),
    JuddlesCurrencyOption("PKR", "₨", "Pakistani Rupee", "Pakistan", "🇵🇰", "South Asia & Nepal"),
    JuddlesCurrencyOption("LKR", "Rs.", "Sri Lankan Rupee", "Sri Lanka", "🇱🇰", "South Asia & Nepal"),
    JuddlesCurrencyOption("BTN", "Nu.", "Bhutanese Ngultrum", "Bhutan", "🇧🇹", "South Asia & Nepal"),
    JuddlesCurrencyOption("MVR", "Rf.", "Maldivian Rufiyaa", "Maldives", "🇲🇻", "South Asia & Nepal"),
    JuddlesCurrencyOption("AFN", "؋", "Afghan Afghani", "Afghanistan", "🇦🇫", "South Asia & Nepal"),

    // East & Southeast Asia
    JuddlesCurrencyOption("JPY", "¥", "Japanese Yen", "Japan", "🇯🇵", "East & SE Asia"),
    JuddlesCurrencyOption("CNY", "¥", "Chinese Yuan", "China", "🇨🇳", "East & SE Asia"),
    JuddlesCurrencyOption("KRW", "₩", "South Korean Won", "South Korea", "🇰🇷", "East & SE Asia"),
    JuddlesCurrencyOption("SGD", "S$", "Singapore Dollar", "Singapore", "🇸🇬", "East & SE Asia"),
    JuddlesCurrencyOption("MYR", "RM", "Malaysian Ringgit", "Malaysia", "🇲🇾", "East & SE Asia"),
    JuddlesCurrencyOption("THB", "฿", "Thai Baht", "Thailand", "🇹🇭", "East & SE Asia"),
    JuddlesCurrencyOption("IDR", "Rp", "Indonesian Rupiah", "Indonesia", "🇮🇩", "East & SE Asia"),
    JuddlesCurrencyOption("PHP", "₱", "Philippine Peso", "Philippines", "🇵🇭", "East & SE Asia"),
    JuddlesCurrencyOption("VND", "₫", "Vietnamese Dong", "Vietnam", "🇻🇳", "East & SE Asia"),
    JuddlesCurrencyOption("HKD", "HK$", "Hong Kong Dollar", "Hong Kong", "🇭🇰", "East & SE Asia"),
    JuddlesCurrencyOption("TWD", "NT$", "New Taiwan Dollar", "Taiwan", "🇹🇼", "East & SE Asia"),
    JuddlesCurrencyOption("MMK", "Ks", "Myanmar Kyat", "Myanmar", "🇲🇲", "East & SE Asia"),
    JuddlesCurrencyOption("KHR", "៛", "Cambodian Riel", "Cambodia", "🇰🇭", "East & SE Asia"),
    JuddlesCurrencyOption("LAK", "₭", "Lao Kip", "Laos", "🇱🇦", "East & SE Asia"),
    JuddlesCurrencyOption("BND", "B$", "Brunei Dollar", "Brunei", "🇧🇳", "East & SE Asia"),
    JuddlesCurrencyOption("MNT", "₮", "Mongolian Tögrög", "Mongolia", "🇲🇳", "East & SE Asia"),

    // Middle East & Central Asia
    JuddlesCurrencyOption("AED", "AED ", "UAE Dirham", "United Arab Emirates", "🇦🇪", "Middle East & Central Asia"),
    JuddlesCurrencyOption("SAR", "SAR ", "Saudi Riyal", "Saudi Arabia", "🇸🇦", "Middle East & Central Asia"),
    JuddlesCurrencyOption("QAR", "QR ", "Qatari Riyal", "Qatar", "🇶🇦", "Middle East & Central Asia"),
    JuddlesCurrencyOption("KWD", "KD ", "Kuwaiti Dinar", "Kuwait", "🇰🇼", "Middle East & Central Asia"),
    JuddlesCurrencyOption("OMR", "OMR ", "Omani Rial", "Oman", "🇴🇲", "Middle East & Central Asia"),
    JuddlesCurrencyOption("BHD", "BD ", "Bahraini Dinar", "Bahrain", "🇧🇭", "Middle East & Central Asia"),
    JuddlesCurrencyOption("TRY", "₺", "Turkish Lira", "Turkey", "🇹🇷", "Middle East & Central Asia"),
    JuddlesCurrencyOption("ILS", "₪", "Israeli Shekel", "Israel", "🇮🇱", "Middle East & Central Asia"),
    JuddlesCurrencyOption("KZT", "₸", "Kazakhstani Tenge", "Kazakhstan", "🇰🇿", "Middle East & Central Asia"),
    JuddlesCurrencyOption("UZS", "so'm ", "Uzbekistani Som", "Uzbekistan", "🇺🇿", "Middle East & Central Asia"),

    // Americas, Europe, Oceania & Africa (Global Major Currencies)
    JuddlesCurrencyOption("USD", "$", "US Dollar", "United States", "🇺🇸", "Americas, Europe & Global"),
    JuddlesCurrencyOption("EUR", "€", "Euro", "Eurozone", "🇪🇺", "Americas, Europe & Global"),
    JuddlesCurrencyOption("GBP", "£", "British Pound", "United Kingdom", "🇬🇧", "Americas, Europe & Global"),
    JuddlesCurrencyOption("CAD", "CA$", "Canadian Dollar", "Canada", "🇨🇦", "Americas, Europe & Global"),
    JuddlesCurrencyOption("AUD", "A$", "Australian Dollar", "Australia", "🇦🇺", "Americas, Europe & Global"),
    JuddlesCurrencyOption("NZD", "NZ$", "New Zealand Dollar", "New Zealand", "🇳🇿", "Americas, Europe & Global"),
    JuddlesCurrencyOption("CHF", "CHF ", "Swiss Franc", "Switzerland", "🇨🇭", "Americas, Europe & Global"),
    JuddlesCurrencyOption("SEK", "kr ", "Swedish Krona", "Sweden", "🇸🇪", "Americas, Europe & Global"),
    JuddlesCurrencyOption("NOK", "kr ", "Norwegian Krone", "Norway", "🇳🇴", "Americas, Europe & Global"),
    JuddlesCurrencyOption("PLN", "zł", "Polish Złoty", "Poland", "🇵🇱", "Americas, Europe & Global"),
    JuddlesCurrencyOption("BRL", "R$", "Brazilian Real", "Brazil", "🇧🇷", "Americas, Europe & Global"),
    JuddlesCurrencyOption("MXN", "MX$", "Mexican Peso", "Mexico", "🇲🇽", "Americas, Europe & Global"),
    JuddlesCurrencyOption("ZAR", "R", "South African Rand", "South Africa", "🇿🇦", "Americas, Europe & Global"),
    JuddlesCurrencyOption("NGN", "₦", "Nigerian Naira", "Nigeria", "🇳🇬", "Americas, Europe & Global"),
    JuddlesCurrencyOption("KES", "KSh ", "Kenyan Shilling", "Kenya", "🇰🇪", "Americas, Europe & Global"),
    JuddlesCurrencyOption("EGP", "E£", "Egyptian Pound", "Egypt", "🇪🇬", "Americas, Europe & Global")
  )

  fun findByCode(code: String): JuddlesCurrencyOption? =
    currencies.firstOrNull { it.code.equals(code, ignoreCase = true) }

  fun symbolFor(code: String): String {
    val found = findByCode(code)
    if (found != null) return found.symbol
    return try {
      Currency.getInstance(code.uppercase(Locale.US)).symbol
    } catch (e: Exception) {
      "$"
    }
  }
}

object FinancialCalculator {

  fun filterByPeriod(
    transactions: List<FinancialTransaction>,
    period: TimePeriodFilter,
    referenceTimeMillis: Long = System.currentTimeMillis()
  ): List<FinancialTransaction> {
    if (period == TimePeriodFilter.ALL_TIME) return transactions
    val cal = Calendar.getInstance().apply { timeInMillis = referenceTimeMillis }
    val targetYear = cal.get(Calendar.YEAR)
    val targetMonth = cal.get(Calendar.MONTH)

    return transactions.filter { tx ->
      val txCal = Calendar.getInstance().apply { timeInMillis = tx.occurredAtEpochMillis }
      val sameYear = txCal.get(Calendar.YEAR) == targetYear
      when (period) {
        TimePeriodFilter.THIS_MONTH -> sameYear && txCal.get(Calendar.MONTH) == targetMonth
        TimePeriodFilter.THIS_YEAR -> sameYear
        TimePeriodFilter.ALL_TIME -> true
      }
    }
  }

  /**
   * Sum of posted INCOME transactions in the selected list.
   * Transfers are strictly excluded from income and expenses.
   */
  fun totalIncomeMinor(transactions: List<FinancialTransaction>): Long =
    transactions.filter { it.type == "INCOME" }.sumOf { it.amountMinor }

  /**
   * Sum of posted EXPENSE transactions in the selected list.
   * Transfers are strictly excluded from income and expenses.
   */
  fun totalExpensesMinor(transactions: List<FinancialTransaction>): Long =
    transactions.filter { it.type == "EXPENSE" }.sumOf { it.amountMinor }

  /**
   * Net cash flow (surplus) = Income - Expenses for the selected period.
   */
  fun netCashFlowMinor(transactions: List<FinancialTransaction>): Long =
    totalIncomeMinor(transactions) - totalExpensesMinor(transactions)

  /**
   * Current balance = Opening balance + all posted income - all posted expenses.
   * Transfers between user-owned accounts net to zero across consolidated balance.
   */
  fun currentBalanceMinor(
    openingBalanceMinor: Long,
    allTransactions: List<FinancialTransaction>
  ): Long {
    val income = totalIncomeMinor(allTransactions)
    val expenses = totalExpensesMinor(allTransactions)
    return openingBalanceMinor + income - expenses
  }

  /**
   * Budget remaining = limit - eligible expenses.
   */
  fun budgetRemainingMinor(limitMinor: Long, spentMinor: Long): Long =
    limitMinor - spentMinor

  /**
   * Budget usage percentage (0..100+); handles zero limits safely.
   */
  fun budgetUsagePercent(limitMinor: Long, spentMinor: Long): Int {
    if (limitMinor <= 0L) return 0
    return ((spentMinor.toDouble() / limitMinor.toDouble()) * 100.0).roundToInt()
  }

  /**
   * Goal progress percentage capped visually at 100% while preserving overfunding in the value.
   */
  fun goalProgressPercent(currentMinor: Long, targetMinor: Long): Int {
    if (targetMinor <= 0L) return 0
    val raw = ((currentMinor.toDouble() / targetMinor.toDouble()) * 100.0).roundToInt()
    return raw.coerceIn(0, 100)
  }

  /**
   * Savings rate = (Income - Expenses) / Income * 100; returns null when income is zero.
   */
  fun savingsRatePercent(transactions: List<FinancialTransaction>): Double? {
    val income = totalIncomeMinor(transactions)
    if (income <= 0L) return null
    val expenses = totalExpensesMinor(transactions)
    return ((income - expenses).toDouble() / income.toDouble()) * 100.0
  }

  fun expenseCategoryBreakdown(transactions: List<FinancialTransaction>): List<CategoryBreakdownItem> {
    val expenses = transactions.filter { it.type == "EXPENSE" }
    val total = expenses.sumOf { it.amountMinor }
    if (total <= 0L) return emptyList()
    return expenses
      .groupBy { it.category.ifBlank { "Other" } }
      .map { (cat, list) ->
        val sum = list.sumOf { it.amountMinor }
        CategoryBreakdownItem(
          category = cat,
          amountMinor = sum,
          percentage = (sum.toFloat() / total.toFloat()) * 100f
        )
      }
      .sortedByDescending { it.amountMinor }
  }

  fun recentMonthsTrend(
    openingBalanceMinor: Long,
    allTransactions: List<FinancialTransaction>,
    monthsCount: Int = 3,
    referenceTimeMillis: Long = System.currentTimeMillis()
  ): List<MonthlyComparisonPoint> {
    val monthFormat = SimpleDateFormat("MMM", Locale.US)
    val result = mutableListOf<MonthlyComparisonPoint>()

    for (offset in (monthsCount - 1) downTo 0) {
      val cal = Calendar.getInstance().apply {
        timeInMillis = referenceTimeMillis
        add(Calendar.MONTH, -offset)
      }
      val year = cal.get(Calendar.YEAR)
      val month = cal.get(Calendar.MONTH)
      val label = monthFormat.format(cal.time)

      val endOfMonthCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
      }
      val endMillis = endOfMonthCal.timeInMillis

      val monthTx = allTransactions.filter { tx ->
        val txCal = Calendar.getInstance().apply { timeInMillis = tx.occurredAtEpochMillis }
        txCal.get(Calendar.YEAR) == year && txCal.get(Calendar.MONTH) == month
      }
      val upToMonthTx = allTransactions.filter { it.occurredAtEpochMillis <= endMillis }

      result.add(
        MonthlyComparisonPoint(
          monthLabel = label,
          incomeMinor = totalIncomeMinor(monthTx),
          expenseMinor = totalExpensesMinor(monthTx),
          balanceEndMinor = currentBalanceMinor(openingBalanceMinor, upToMonthTx)
        )
      )
    }
    return result
  }

  fun formatCurrencyMinor(
    amountMinor: Long,
    currencyCode: String = "USD",
    hideAmounts: Boolean = false,
    includeCents: Boolean = true,
    showSign: Boolean = false
  ): String {
    if (hideAmounts) return "••••••"
    val symbol = JuddlesCurrencyCatalog.symbolFor(currencyCode)
    val isNegative = amountMinor < 0L
    val absMinor = abs(amountMinor)
    val major = absMinor / 100.0
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
      if (includeCents) {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
      } else {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
      }
    }
    val formatted = numberFormat.format(if (includeCents) major else (absMinor / 100L))
    val prefix = when {
      isNegative -> "-"
      showSign && amountMinor > 0L -> "+"
      else -> ""
    }
    return "$prefix$symbol$formatted"
  }

  fun formatRelativeDate(epochMillis: Long): String {
    if (epochMillis <= 0L) return "Today"
    val nowCal = Calendar.getInstance()
    val txCal = Calendar.getInstance().apply { timeInMillis = epochMillis }
    val timeFmt = SimpleDateFormat("h:mm a", Locale.US)
    val dateFmt = SimpleDateFormat("MMM d, yyyy", Locale.US)

    val sameYear = nowCal.get(Calendar.YEAR) == txCal.get(Calendar.YEAR)
    val sameDay = sameYear && nowCal.get(Calendar.DAY_OF_YEAR) == txCal.get(Calendar.DAY_OF_YEAR)
    val yesterday = sameYear && (nowCal.get(Calendar.DAY_OF_YEAR) - txCal.get(Calendar.DAY_OF_YEAR) == 1)

    return when {
      sameDay -> "Today, ${timeFmt.format(Date(epochMillis))}"
      yesterday -> "Yesterday, ${timeFmt.format(Date(epochMillis))}"
      else -> dateFmt.format(Date(epochMillis))
    }
  }
}
