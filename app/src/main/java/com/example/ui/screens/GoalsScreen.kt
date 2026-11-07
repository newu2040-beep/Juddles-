package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.ui.components.TargetBullseyeIllustration
import com.example.ui.theme.JuddlesAmber
import com.example.ui.theme.JuddlesAmberSoft
import com.example.ui.theme.JuddlesBlue
import com.example.ui.theme.JuddlesBlueSoft
import com.example.ui.theme.JuddlesBrightGreen
import com.example.ui.theme.JuddlesCoral
import com.example.ui.theme.JuddlesCoralSoft
import com.example.ui.theme.JuddlesEvergreen
import com.example.ui.theme.JuddlesFreshGreen
import com.example.ui.theme.JuddlesIceGreen
import com.example.ui.theme.JuddlesMint
import com.example.ui.viewmodel.JuddlesDashboardState

/**
 * Screen 3 — Goals Screen matching the third screen in the reference mockup.
 */
@Composable
fun GoalsScreen(
  state: JuddlesDashboardState,
  onBackClick: () -> Unit,
  onCreateNewGoalClick: () -> Unit,
  onGoalClick: (FinancialGoal) -> Unit,
  onEditMonthlyTargetClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showOverflowMenu by remember { mutableStateOf(false) }
  var filterStatus by remember { mutableStateOf("ALL") } // ALL, ACTIVE, COMPLETED
  val currency = state.profile.preferredCurrency
  val hide = state.profile.hideAmounts

  val filteredGoals = remember(state.goals, filterStatus) {
    when (filterStatus) {
      "ACTIVE" -> state.goals.filter { it.status == "ACTIVE" }
      "COMPLETED" -> state.goals.filter { it.status == "COMPLETED" || it.currentMinor >= it.targetMinor }
      else -> state.goals
    }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = 20.dp,
      end = 20.dp,
      top = 12.dp,
      bottom = 104.dp
    ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Top Navigation Row (< Back & Overflow Menu ⋮)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onBackClick)
            .testTag("goals_back_button"),
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 3.dp
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier
                .padding(start = 6.dp)
                .size(18.dp)
            )
          }
        }

        Box {
          Surface(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .clickable { showOverflowMenu = true }
              .testTag("goals_menu_button"),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Goal options",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          DropdownMenu(
            expanded = showOverflowMenu,
            onDismissRequest = { showOverflowMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text("All Goals") },
              onClick = {
                filterStatus = "ALL"
                showOverflowMenu = false
              }
            )
            DropdownMenuItem(
              text = { Text("Active Only") },
              onClick = {
                filterStatus = "ACTIVE"
                showOverflowMenu = false
              }
            )
            DropdownMenuItem(
              text = { Text("Completed Only") },
              onClick = {
                filterStatus = "COMPLETED"
                showOverflowMenu = false
              }
            )
            DropdownMenuItem(
              text = { Text("Edit Monthly Goal Target") },
              onClick = {
                showOverflowMenu = false
                onEditMonthlyTargetClick()
              }
            )
          }
        }
      }
    }

    // 2. Hero Target Bullseye Illustration + "Goals" + "Turn your dreams into goals"
    item {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        TargetBullseyeIllustration()
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Goals",
          style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
          ),
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Turn your dreams into goals",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // 3. Primary Pill Button: "+ Create New Goal" (#075B43 Evergreen)
    item {
      Button(
        onClick = onCreateNewGoalClick,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = JuddlesEvergreen,
          contentColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .shadow(8.dp, RoundedCornerShape(28.dp))
          .testTag("create_new_goal_button")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = null,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Create New Goal",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
    }

    // 4. Goal Cards List
    if (filteredGoals.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(22.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "No goals yet",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Tap '+ Create New Goal' above to set a savings target for travel, a home, emergency fund, or life milestone.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    } else {
      items(filteredGoals, key = { it.id.ifBlank { it.title } }) { goal ->
        GoalCardItem(
          goal = goal,
          currency = currency,
          hideAmounts = hide,
          onClick = { onGoalClick(goal) }
        )
      }
    }

    // 5. Bottom Motivational Banner Card ("You're on track! Keep going to achieve more.")
    item {
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = JuddlesMint,
        shadowElevation = 2.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(JuddlesAmberSoft),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = "Trophy",
              tint = JuddlesAmber,
              modifier = Modifier.size(28.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "You're on track!",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Keep going to achieve more.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
private fun GoalCardItem(
  goal: FinancialGoal,
  currency: String,
  hideAmounts: Boolean,
  onClick: () -> Unit
) {
  val percent = FinancialCalculator.goalProgressPercent(goal.currentMinor, goal.targetMinor)
  val isComplete = percent >= 100 || goal.status == "COMPLETED"

  val (iconVec, iconBg, iconTint) = when (goal.iconKey.uppercase()) {
    "TRAVEL" -> Triple(Icons.Default.FlightTakeoff, JuddlesBlueSoft, JuddlesBlue)
    "HOME" -> Triple(Icons.Default.Home, JuddlesCoralSoft, JuddlesCoral)
    "CAR" -> Triple(Icons.Default.DirectionsCar, JuddlesBlueSoft, JuddlesBlue)
    "EDUCATION" -> Triple(Icons.Default.School, JuddlesAmberSoft, JuddlesAmber)
    "TROPHY" -> Triple(Icons.Default.EmojiEvents, JuddlesAmberSoft, JuddlesAmber)
    else -> Triple(Icons.Default.Savings, JuddlesIceGreen, JuddlesEvergreen)
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(
        elevation = 6.dp,
        shape = RoundedCornerShape(22.dp),
        ambientColor = Color.Black.copy(alpha = 0.08f)
      )
      .clip(RoundedCornerShape(22.dp))
      .clickable(onClick = onClick)
      .testTag("goal_card_${goal.title}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left Icon Badge
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(CircleShape)
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = iconVec,
          contentDescription = goal.title,
          tint = iconTint,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Middle: Title, "$Current / $Target", Slim Horizontal Progress Bar
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = goal.title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = buildAnnotatedString {
            withStyle(
              SpanStyle(
                fontWeight = FontWeight.Bold,
                color = if (isComplete) JuddlesEvergreen else MaterialTheme.colorScheme.onSurface
              )
            ) {
              append(
                FinancialCalculator.formatCurrencyMinor(
                  amountMinor = goal.currentMinor,
                  currencyCode = currency,
                  hideAmounts = hideAmounts,
                  includeCents = false
                )
              )
            }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
              append(
                " / ${
                  FinancialCalculator.formatCurrencyMinor(
                    amountMinor = goal.targetMinor,
                    currencyCode = currency,
                    hideAmounts = hideAmounts,
                    includeCents = false
                  )
                }"
              )
            }
          },
          style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
          progress = { (percent / 100f).coerceIn(0f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = JuddlesBrightGreen,
          trackColor = JuddlesMint,
          strokeCap = StrokeCap.Round
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Right: Evergreen Checkmark Circle if 100%, or bold percentage text (50%, 33%)
      if (isComplete) {
        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(JuddlesEvergreen),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Goal completed",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      } else {
        Text(
          text = "$percent%",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}
