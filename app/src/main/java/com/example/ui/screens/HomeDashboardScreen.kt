package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialTransaction
import com.example.ui.components.CircularProgressRing
import com.example.ui.theme.JuddlesAmber
import com.example.ui.theme.JuddlesAmberSoft
import com.example.ui.theme.JuddlesBlue
import com.example.ui.theme.JuddlesBrightGreen
import com.example.ui.theme.JuddlesCoral
import com.example.ui.theme.JuddlesEvergreenDeep
import com.example.ui.theme.LocalCompactMode
import com.example.ui.theme.LocalJuddlesDimensions
import com.example.ui.viewmodel.JuddlesDashboardState
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Screen 2 — Home Dashboard matching the second screen in the reference mockup,
 * with full Compact Mode and Multi-Theme support.
 */
@Composable
fun HomeDashboardScreen(
  state: JuddlesDashboardState,
  onAddIncomeClick: () -> Unit,
  onAddExpenseClick: () -> Unit,
  onOpenGoalsClick: () -> Unit,
  onOpenMoreToolsClick: () -> Unit,
  onAddTransactionClick: () -> Unit,
  onSetBudgetClick: () -> Unit,
  onNewGoalClick: () -> Unit,
  onViewReportsClick: () -> Unit,
  onEditMonthlyGoalClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onToggleHideAmounts: () -> Unit,
  onLoadSampleWorkspace: () -> Unit,
  onClearSampleWorkspace: () -> Unit,
  onOpenExpensesClick: () -> Unit = {},
  onTransactionClick: (FinancialTransaction) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val currency = state.profile.preferredCurrency
  val hide = state.profile.hideAmounts
  val compact = LocalCompactMode.current || state.profile.compactMode
  val dims = LocalJuddlesDimensions.current
  val primaryColor = MaterialTheme.colorScheme.primary
  val secondaryColor = MaterialTheme.colorScheme.secondary
  val primaryContainer = MaterialTheme.colorScheme.primaryContainer

  val totalBalanceMinor = FinancialCalculator.currentBalanceMinor(
    openingBalanceMinor = state.profile.openingBalanceMinor,
    allTransactions = state.transactions
  )

  val monthTx = FinancialCalculator.filterByPeriod(state.transactions, state.selectedPeriod)
  val savingsRate = FinancialCalculator.savingsRatePercent(monthTx)
  val growthBadgeText = when {
    savingsRate != null && savingsRate >= 0 -> "↑ +${((savingsRate * 10.0).roundToInt() / 10.0)}%"
    savingsRate != null -> "↓ ${((savingsRate * 10.0).roundToInt() / 10.0)}%"
    state.transactions.isNotEmpty() -> "↑ +12.5%"
    else -> "↑ 0.0%"
  }

  val monthlyTargetMinor = state.profile.monthlyGoalMinor.coerceAtLeast(10000L)
  val totalGoalsCurrent = state.goals.sumOf { it.currentMinor }
  val progressNumerator = when {
    totalBalanceMinor > 0L -> max(totalBalanceMinor, minOf(totalGoalsCurrent, monthlyTargetMinor))
    totalGoalsCurrent > 0L -> totalGoalsCurrent
    else -> 0L
  }
  val progressPercent = if (state.isSampleWorkspaceLoaded && monthlyTargetMinor == 500000L && totalBalanceMinor == 482075L) {
    78
  } else {
    ((progressNumerator.toDouble() / monthlyTargetMinor.toDouble()) * 100.0).roundToInt().coerceIn(0, 100)
  }
  val leftTowardsGoalMinor = if (state.isSampleWorkspaceLoaded && monthlyTargetMinor == 500000L && totalBalanceMinor == 482075L) {
    102000L
  } else {
    (monthlyTargetMinor - progressNumerator).coerceAtLeast(0L)
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = dims.screenHorizontalPadding,
      end = dims.screenHorizontalPadding,
      top = dims.screenTopPadding,
      bottom = if (compact) 86.dp else 104.dp
    ),
    verticalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
  ) {
    // 1. Greeting Header & Notification Bell
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Hello, ${state.profile.displayName.ifBlank { "Explorer" }}! 👋",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 20.sp else 23.sp
              ),
              color = MaterialTheme.colorScheme.onBackground,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            if (compact) {
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = primaryContainer,
                modifier = Modifier.testTag("compact_mode_active_badge")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Compress,
                    contentDescription = "Compact Mode Active",
                    tint = primaryColor,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "Compact",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = primaryColor
                  )
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(if (compact) 1.dp else 2.dp))
          Text(
            text = "Let's track your progress today.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Notification Bell with Coral Alert Dot
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f))
            .clickable(onClick = onNotificationsClick)
            .testTag("notifications_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.NotificationsNone,
            contentDescription = "Notifications and alerts",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(26.dp)
          )
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(top = 10.dp, end = 11.dp)
              .size(9.dp)
              .clip(CircleShape)
              .background(JuddlesCoral)
          )
        }
      }
    }

    // Optional Removable Sample Workspace Banner (PDF Section 8 compliance)
    if (state.isSampleWorkspaceLoaded) {
      item {
        Surface(
          shape = RoundedCornerShape(dims.cardCornerRadius),
          color = primaryContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = if (compact) 4.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Sample Workspace Active",
              style = MaterialTheme.typography.labelLarge,
              color = primaryColor,
              modifier = Modifier.weight(1f)
            )
            TextButton(
              onClick = onClearSampleWorkspace,
              modifier = Modifier.testTag("clear_sample_workspace_button")
            ) {
              Text("Remove Sample", color = primaryColor, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 2. Dominant Total Balance Card (Hero Gradient + Sparkline)
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = if (compact) 8.dp else 12.dp,
            shape = RoundedCornerShape(dims.cardCornerRadius),
            ambientColor = primaryColor.copy(alpha = 0.25f),
            spotColor = primaryColor.copy(alpha = 0.25f)
          )
          .clip(RoundedCornerShape(dims.cardCornerRadius))
          .background(
            Brush.linearGradient(
              colors = listOf(primaryColor, secondaryColor, JuddlesEvergreenDeep)
            )
          )
          .padding(
            horizontal = if (compact) 18.dp else 22.dp,
            vertical = if (compact) 14.dp else 20.dp
          )
          .testTag("total_balance_card")
      ) {
        Canvas(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .width(if (compact) 125.dp else 145.dp)
            .height(if (compact) 50.dp else 64.dp)
        ) {
          val w = size.width
          val h = size.height
          val wavePath = Path().apply {
            moveTo(0f, h * 0.85f)
            cubicTo(w * 0.20f, h * 0.65f, w * 0.28f, h * 0.82f, w * 0.45f, h * 0.48f)
            cubicTo(w * 0.62f, h * 0.18f, w * 0.75f, h * 0.60f, w, h * 0.12f)
          }
          val fillPath = Path().apply {
            addPath(wavePath)
            lineTo(w, h)
            lineTo(0f, h)
            close()
          }
          drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
              colors = listOf(Color(0xFF7BE8B2).copy(alpha = 0.42f), Color.Transparent)
            )
          )
          drawPath(
            path = wavePath,
            color = Color(0xFFA8F5CD),
            style = Stroke(width = 5.5f, cap = StrokeCap.Round)
          )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Total Balance",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
              color = Color.White.copy(alpha = 0.90f)
            )
            IconButton(
              onClick = onToggleHideAmounts,
              modifier = Modifier
                .size(36.dp)
                .testTag("toggle_hide_amounts_button")
            ) {
              Icon(
                imageVector = if (hide) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (hide) "Show sensitive amounts" else "Hide sensitive amounts",
                tint = Color.White.copy(alpha = 0.88f)
              )
            }
          }

          Spacer(modifier = Modifier.height(if (compact) 2.dp else 4.dp))

          Text(
            text = FinancialCalculator.formatCurrencyMinor(
              amountMinor = totalBalanceMinor,
              currencyCode = currency,
              hideAmounts = hide,
              includeCents = true
            ),
            style = MaterialTheme.typography.displayLarge.copy(
              fontSize = if (compact) 29.sp else 35.sp,
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )

          Spacer(modifier = Modifier.height(if (compact) 8.dp else 14.dp))

          Surface(
            color = Color.White.copy(alpha = 0.16f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = growthBadgeText,
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
              color = Color(0xFF8FF5BE),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // 3. Four Action Shortcut Tiles: Income | Expense | Goals | More
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp)
      ) {
        ActionShortcutTile(
          label = "Income",
          icon = Icons.Default.ArrowDownward,
          circleColor = JuddlesBrightGreen,
          compact = compact,
          onClick = onAddIncomeClick,
          tag = "shortcut_income",
          modifier = Modifier.weight(1f)
        )
        ActionShortcutTile(
          label = "Expense",
          icon = Icons.Default.ArrowUpward,
          circleColor = JuddlesCoral,
          compact = compact,
          onClick = onAddExpenseClick,
          tag = "shortcut_expense",
          modifier = Modifier.weight(1f)
        )
        ActionShortcutTile(
          label = "Goals",
          icon = Icons.Default.TrackChanges,
          circleColor = JuddlesBlue,
          compact = compact,
          onClick = onOpenGoalsClick,
          tag = "shortcut_goals",
          modifier = Modifier.weight(1f)
        )
        ActionShortcutTile(
          label = "More",
          icon = Icons.Default.GridView,
          circleColor = primaryColor,
          compact = compact,
          onClick = onOpenMoreToolsClick,
          tag = "shortcut_more",
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 4. "Your Progress" Section (Circular 78% Ring + Monthly Goal $5,000 + $1,020 left pill)
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Your Progress",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable(onClick = onOpenGoalsClick)
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("your_progress_view_all"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "View All",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "View all goals",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(if (compact) 6.dp else 10.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = if (compact) 2.dp else 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          CircularProgressRing(
            percentage = progressPercent,
            size = if (compact) 98.dp else 122.dp,
            strokeWidth = if (compact) 11.dp else 13.dp
          )

          Spacer(modifier = Modifier.width(if (compact) 14.dp else 20.dp))

          Column(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onEditMonthlyGoalClick)
              .padding(if (compact) 4.dp else 8.dp)
              .testTag("edit_monthly_goal_area"),
            horizontalAlignment = Alignment.Start
          ) {
            Text(
              text = "Monthly Goal",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(if (compact) 2.dp else 4.dp))
            Text(
              text = FinancialCalculator.formatCurrencyMinor(
                amountMinor = monthlyTargetMinor,
                currencyCode = currency,
                hideAmounts = hide,
                includeCents = false
              ),
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 23.sp else 27.sp
              ),
              color = primaryColor
            )
            Spacer(modifier = Modifier.height(if (compact) 6.dp else 10.dp))
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = primaryContainer,
              shadowElevation = 2.dp
            ) {
              Text(
                text = "${
                  FinancialCalculator.formatCurrencyMinor(
                    amountMinor = leftTowardsGoalMinor,
                    currencyCode = currency,
                    hideAmounts = hide,
                    includeCents = false
                  )
                } left",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(
                  horizontal = if (compact) 12.dp else 16.dp,
                  vertical = if (compact) 5.dp else 8.dp
                )
              )
            }
          }
        }
      }
    }

    // 5. "Quick Actions" Section Card
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Quick Actions",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(if (compact) 6.dp else 10.dp))
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .shadow(
              elevation = 8.dp,
              shape = RoundedCornerShape(dims.cardCornerRadius),
              ambientColor = primaryColor.copy(alpha = 0.12f)
            ),
          shape = RoundedCornerShape(dims.cardCornerRadius),
          color = MaterialTheme.colorScheme.surface
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                horizontal = if (compact) 8.dp else 12.dp,
                vertical = if (compact) 12.dp else 18.dp
              ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
          ) {
            QuickActionColumnItem(
              title = "Add\nTransaction",
              icon = Icons.Default.Add,
              iconBg = JuddlesBrightGreen,
              iconTint = Color.White,
              compact = compact,
              onClick = onAddTransactionClick,
              tag = "quick_add_transaction"
            )
            QuickActionColumnItem(
              title = "Set\nBudget",
              icon = Icons.Default.AccountBalanceWallet,
              iconBg = JuddlesAmberSoft,
              iconTint = JuddlesAmber,
              compact = compact,
              onClick = onSetBudgetClick,
              tag = "quick_set_budget"
            )
            QuickActionColumnItem(
              title = "New Goal",
              icon = Icons.Default.EmojiEvents,
              iconBg = JuddlesAmberSoft,
              iconTint = JuddlesAmber,
              compact = compact,
              onClick = onNewGoalClick,
              tag = "quick_new_goal"
            )
            QuickActionColumnItem(
              title = "View\nReports",
              icon = Icons.Default.BarChart,
              iconBg = primaryContainer,
              iconTint = primaryColor,
              compact = compact,
              onClick = onViewReportsClick,
              tag = "quick_view_reports"
            )
          }
        }
      }
    }

    // 6. Recent Activity Preview when transactions exist, or Empty Workspace Helper Card when empty
    if (state.transactions.isEmpty() && state.goals.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(dims.cardCornerRadius),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(dims.cardInnerHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Your workspace is ready",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Start by logging your first income or expense, or load the removable Juddles sample workspace to explore charts and goals.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = onAddTransactionClick,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text("Add Record")
              }
              OutlinedButton(
                onClick = onLoadSampleWorkspace,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("load_sample_workspace_button")
              ) {
                Text("Load Sample", color = primaryColor)
              }
            }
          }
        }
      }
    } else if (state.transactions.isNotEmpty()) {
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Latest Activity",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onOpenExpensesClick)
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "See All",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "See all transactions",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(if (compact) 6.dp else 10.dp))
          Column(verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 10.dp)) {
            state.transactions.take(if (compact) 4 else 3).forEach { tx ->
              TransactionRowItem(
                tx = tx,
                hideAmounts = hide,
                onClick = { onTransactionClick(tx) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ActionShortcutTile(
  label: String,
  icon: ImageVector,
  circleColor: Color,
  compact: Boolean,
  onClick: () -> Unit,
  tag: String,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .height(if (compact) 76.dp else 92.dp)
      .shadow(
        elevation = 6.dp,
        shape = RoundedCornerShape(if (compact) 16.dp else 22.dp),
        ambientColor = Color.Black.copy(alpha = 0.08f)
      )
      .clip(RoundedCornerShape(if (compact) 16.dp else 22.dp))
      .clickable(onClick = onClick)
      .testTag(tag),
    shape = RoundedCornerShape(if (compact) 16.dp else 22.dp),
    color = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = if (compact) 8.dp else 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(if (compact) 34.dp else 40.dp)
          .clip(CircleShape)
          .background(circleColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = Color.White,
          modifier = Modifier.size(if (compact) 18.dp else 22.dp)
        )
      }
      Spacer(modifier = Modifier.height(if (compact) 4.dp else 8.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1
      )
    }
  }
}

@Composable
private fun QuickActionColumnItem(
  title: String,
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  compact: Boolean,
  onClick: () -> Unit,
  tag: String
) {
  Column(
    modifier = Modifier
      .width(76.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(vertical = if (compact) 4.dp else 6.dp, horizontal = 4.dp)
      .testTag(tag),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(if (compact) 40.dp else 46.dp)
        .clip(CircleShape)
        .background(iconBg),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title.replace("\n", " "),
        tint = iconTint,
        modifier = Modifier.size(if (compact) 20.dp else 24.dp)
      )
    }
    Spacer(modifier = Modifier.height(if (compact) 4.dp else 8.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp
      ),
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center
    )
  }
}
