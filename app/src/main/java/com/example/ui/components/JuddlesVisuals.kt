package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryBreakdownItem
import com.example.data.model.MonthlyComparisonPoint
import com.example.ui.theme.JuddlesAmber
import com.example.ui.theme.JuddlesBlue
import com.example.ui.theme.JuddlesBrightGreen
import com.example.ui.theme.JuddlesCoral
import com.example.ui.theme.JuddlesEvergreen
import com.example.ui.theme.JuddlesFreshGreen
import com.example.ui.theme.JuddlesMint
import com.example.ui.theme.JuddlesMuted
import com.example.ui.theme.JuddlesTealSoft
import com.example.ui.theme.LocalCompactMode
import kotlin.math.max

enum class MainNavTab(val label: String) {
  HOME("Home"),
  STATS("Stats"),
  GOALS("Goals"),
  PROFILE("Profile")
}

/**
 * Canvas background with soft organic radial accents that adapt to the active theme.
 */
@Composable
fun JuddlesBackgroundWrapper(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val bgColor = MaterialTheme.colorScheme.background
  val primaryContainer = MaterialTheme.colorScheme.primaryContainer
  val secondary = MaterialTheme.colorScheme.secondary
  val isLight = bgColor.red > 0.45f

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(bgColor)
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val topAlpha = if (isLight) 0.48f else 0.22f
      val midAlpha = if (isLight) 0.35f else 0.16f
      // Top-right organic wave glow
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(secondary.copy(alpha = topAlpha * 0.65f), Color.Transparent),
          center = Offset(size.width * 0.88f, size.height * 0.04f),
          radius = size.width * 0.55f
        ),
        center = Offset(size.width * 0.88f, size.height * 0.04f),
        radius = size.width * 0.55f
      )
      // Left-middle subtle orb
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(primaryContainer.copy(alpha = midAlpha), Color.Transparent),
          center = Offset(size.width * 0.05f, size.height * 0.36f),
          radius = size.width * 0.42f
        ),
        center = Offset(size.width * 0.05f, size.height * 0.36f),
        radius = size.width * 0.42f
      )
      // Bottom-right soft glow
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(primaryContainer.copy(alpha = topAlpha), Color.Transparent),
          center = Offset(size.width * 0.85f, size.height * 0.88f),
          radius = size.width * 0.5f
        ),
        center = Offset(size.width * 0.85f, size.height * 0.88f),
        radius = size.width * 0.5f
      )
    }
    content()
  }
}

/**
 * Juddles Brand Mark + Wordmark + "Track • Plan • Grow" tagline (Screen 1).
 */
@Composable
fun JuddlesBrandHeader(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Canvas(
      modifier = Modifier
        .size(56.dp)
        .semantics { contentDescription = "Juddles growth logo" }
    ) {
      val w = size.width
      val h = size.height
      drawRoundRect(
        color = Color(0xFF60C892),
        topLeft = Offset(w * 0.18f, h * 0.56f),
        size = Size(w * 0.15f, h * 0.32f),
        cornerRadius = CornerRadius(6f, 6f)
      )
      drawRoundRect(
        color = JuddlesFreshGreen,
        topLeft = Offset(w * 0.42f, h * 0.40f),
        size = Size(w * 0.15f, h * 0.48f),
        cornerRadius = CornerRadius(6f, 6f)
      )
      drawRoundRect(
        color = JuddlesEvergreen,
        topLeft = Offset(w * 0.66f, h * 0.22f),
        size = Size(w * 0.15f, h * 0.66f),
        cornerRadius = CornerRadius(6f, 6f)
      )
      val arrowPath = Path().apply {
        moveTo(w * 0.16f, h * 0.46f)
        lineTo(w * 0.44f, h * 0.26f)
        lineTo(w * 0.55f, h * 0.33f)
        lineTo(w * 0.78f, h * 0.10f)
      }
      drawPath(
        path = arrowPath,
        color = JuddlesEvergreen,
        style = Stroke(width = 6.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
      )
      val headPath = Path().apply {
        moveTo(w * 0.64f, h * 0.10f)
        lineTo(w * 0.80f, h * 0.10f)
        lineTo(w * 0.80f, h * 0.26f)
      }
      drawPath(
        path = headPath,
        color = JuddlesEvergreen,
        style = Stroke(width = 6.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
      )
    }

    Text(
      text = "juddles",
      style = MaterialTheme.typography.displayLarge.copy(
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-1).sp
      ),
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = "Track • Plan • Grow",
      style = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.4.sp
      ),
      color = MaterialTheme.colorScheme.primary
    )
  }
}

/**
 * Custom 3D-style Canvas illustration for Welcome Screen:
 * Mint/emerald piggy bank with gold coin on top, botanical leaves, 3 ascending bars, and upward growth arrow.
 */
@Composable
fun PiggyBankGrowthIllustration(modifier: Modifier = Modifier) {
  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(210.dp)
      .semantics { contentDescription = "Piggy bank and ascending financial growth chart" }
  ) {
    val w = size.width
    val h = size.height

    drawOval(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFF075B43).copy(alpha = 0.18f), Color.Transparent),
        center = Offset(w * 0.48f, h * 0.88f),
        radius = w * 0.42f
      ),
      topLeft = Offset(w * 0.10f, h * 0.80f),
      size = Size(w * 0.78f, h * 0.15f)
    )

    val leafColor1 = Color(0xFF7ED9A4)
    val leafColor2 = Color(0xFF47B37A)
    drawOval(
      color = leafColor1.copy(alpha = 0.65f),
      topLeft = Offset(w * 0.10f, h * 0.34f),
      size = Size(w * 0.11f, h * 0.22f)
    )
    drawOval(
      color = leafColor2.copy(alpha = 0.7f),
      topLeft = Offset(w * 0.24f, h * 0.18f),
      size = Size(w * 0.09f, h * 0.18f)
    )

    val barWidths = w * 0.085f
    val barBottom = h * 0.85f
    val barX1 = w * 0.56f
    val barX2 = w * 0.68f
    val barX3 = w * 0.80f
    val barH1 = h * 0.24f
    val barH2 = h * 0.36f
    val barH3 = h * 0.50f

    listOf(
      Triple(barX1, barH1, Color(0xFF5DC88E)),
      Triple(barX2, barH2, Color(0xFF2B9E6B)),
      Triple(barX3, barH3, JuddlesEvergreen)
    ).forEach { (bx, bh, bColor) ->
      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(bColor.copy(alpha = 0.95f), JuddlesEvergreen)
        ),
        topLeft = Offset(bx, barBottom - bh),
        size = Size(barWidths, bh),
        cornerRadius = CornerRadius(14f, 14f)
      )
      drawRoundRect(
        color = Color.White.copy(alpha = 0.25f),
        topLeft = Offset(bx + 4f, barBottom - bh + 6f),
        size = Size(barWidths * 0.28f, bh - 12f),
        cornerRadius = CornerRadius(8f, 8f)
      )
    }

    val curve = Path().apply {
      moveTo(w * 0.45f, h * 0.52f)
      quadraticTo(w * 0.66f, h * 0.34f, w * 0.86f, h * 0.11f)
    }
    drawPath(
      path = curve,
      color = JuddlesEvergreen,
      style = Stroke(width = 9f, cap = StrokeCap.Round)
    )
    val arrowHead = Path().apply {
      moveTo(w * 0.78f, h * 0.12f)
      lineTo(w * 0.87f, h * 0.10f)
      lineTo(w * 0.85f, h * 0.20f)
    }
    drawPath(
      path = arrowHead,
      color = JuddlesEvergreen,
      style = Stroke(width = 9f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    val footColor = Color(0xFF1E7E57)
    drawRoundRect(
      color = footColor,
      topLeft = Offset(w * 0.20f, h * 0.74f),
      size = Size(w * 0.065f, h * 0.12f),
      cornerRadius = CornerRadius(14f, 14f)
    )
    drawRoundRect(
      color = footColor,
      topLeft = Offset(w * 0.36f, h * 0.74f),
      size = Size(w * 0.065f, h * 0.12f),
      cornerRadius = CornerRadius(14f, 14f)
    )

    val coinCenter = Offset(w * 0.31f, h * 0.29f)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFE082), Color(0xFFF39C12)),
        center = coinCenter,
        radius = w * 0.055f
      ),
      center = coinCenter,
      radius = w * 0.055f
    )
    drawCircle(
      color = Color(0xFFD68910),
      center = coinCenter,
      radius = w * 0.040f,
      style = Stroke(width = 3f)
    )

    val bodyRectTopLeft = Offset(w * 0.14f, h * 0.34f)
    val bodySize = Size(w * 0.34f, h * 0.45f)
    drawOval(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFF8BE2B0), Color(0xFF2E9E6B), Color(0xFF0E6245)),
        center = Offset(w * 0.26f, h * 0.48f),
        radius = w * 0.24f
      ),
      topLeft = bodyRectTopLeft,
      size = bodySize
    )

    drawRoundRect(
      color = Color(0xFF063D2B),
      topLeft = Offset(w * 0.26f, h * 0.355f),
      size = Size(w * 0.09f, h * 0.025f),
      cornerRadius = CornerRadius(8f, 8f)
    )

    drawOval(
      color = Color(0xFF39AB76),
      topLeft = Offset(w * 0.34f, h * 0.29f),
      size = Size(w * 0.075f, h * 0.12f)
    )

    drawOval(
      brush = Brush.linearGradient(
        colors = listOf(Color(0xFF7AD8A4), Color(0xFF288E5F))
      ),
      topLeft = Offset(w * 0.43f, h * 0.48f),
      size = Size(w * 0.08f, h * 0.16f)
    )
    drawCircle(
      color = Color(0xFF074934),
      center = Offset(w * 0.475f, h * 0.54f),
      radius = 4.5f
    )
    drawCircle(
      color = Color(0xFF074934),
      center = Offset(w * 0.495f, h * 0.56f),
      radius = 4.5f
    )

    drawCircle(
      color = Color(0xFF073B2A),
      center = Offset(w * 0.405f, h * 0.47f),
      radius = 6f
    )
    drawCircle(
      color = Color.White,
      center = Offset(w * 0.402f, h * 0.464f),
      radius = 2.2f
    )
  }
}

/**
 * Custom 3D-style Bullseye Target with Arrow Illustration for Screen 3 (Goals).
 */
@Composable
fun TargetBullseyeIllustration(modifier: Modifier = Modifier) {
  val isCompact = LocalCompactMode.current
  val primaryColor = MaterialTheme.colorScheme.primary
  val secondaryColor = MaterialTheme.colorScheme.secondary

  Canvas(
    modifier = modifier
      .size(if (isCompact) 108.dp else 136.dp)
      .semantics { contentDescription = "Goals target bullseye with arrow" }
  ) {
    val c = Offset(size.width * 0.48f, size.height * 0.50f)
    val maxR = size.minDimension * 0.42f

    drawOval(
      brush = Brush.radialGradient(
        colors = listOf(primaryColor.copy(alpha = 0.22f), Color.Transparent),
        center = Offset(c.x, size.height * 0.92f),
        radius = maxR * 0.9f
      ),
      topLeft = Offset(c.x - maxR * 0.8f, size.height * 0.84f),
      size = Size(maxR * 1.6f, size.height * 0.14f)
    )

    drawCircle(
      color = primaryColor,
      center = Offset(c.x - 4f, c.y + 4f),
      radius = maxR
    )
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(secondaryColor.copy(alpha = 0.85f), secondaryColor),
        center = Offset(c.x - 10f, c.y - 10f),
        radius = maxR * 1.2f
      ),
      center = c,
      radius = maxR
    )
    drawCircle(
      color = Color(0xFFF7F5EA),
      center = c,
      radius = maxR * 0.74f
    )
    drawCircle(
      color = secondaryColor,
      center = c,
      radius = maxR * 0.50f
    )
    drawCircle(
      color = Color(0xFFF7F5EA),
      center = c,
      radius = maxR * 0.28f
    )
    drawCircle(
      color = primaryColor,
      center = c,
      radius = maxR * 0.13f
    )

    val tail = Offset(size.width * 0.86f, size.height * 0.14f)
    drawLine(
      color = primaryColor,
      start = c,
      end = tail,
      strokeWidth = 7f,
      cap = StrokeCap.Round
    )
    val featherPath = Path().apply {
      moveTo(size.width * 0.74f, size.height * 0.14f)
      lineTo(size.width * 0.86f, size.height * 0.04f)
      lineTo(size.width * 0.90f, size.height * 0.12f)
      lineTo(size.width * 0.96f, size.height * 0.16f)
      lineTo(size.width * 0.86f, size.height * 0.26f)
      close()
    }
    drawPath(path = featherPath, color = secondaryColor)

    val sphereCenter = Offset(size.width * 0.90f, size.height * 0.72f)
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFBDF2D1), secondaryColor),
        center = Offset(sphereCenter.x - 4f, sphereCenter.y - 4f),
        radius = 22f
      ),
      center = sphereCenter,
      radius = 16f
    )
  }
}

/**
 * Floating Rounded Capsule Navigation Bar matching all 4 signed-in screens in the mockup:
 * Home | Stats | [ + Primary FAB ] | Goals | Profile
 */
@Composable
fun JuddlesFloatingBottomBar(
  selectedTab: MainNavTab,
  onSelectTab: (MainNavTab) -> Unit,
  onCentralAddClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isCompact = LocalCompactMode.current
  val primary = MaterialTheme.colorScheme.primary
  val secondary = MaterialTheme.colorScheme.secondary

  Box(
    modifier = modifier
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.navigationBars)
      .padding(horizontal = if (isCompact) 12.dp else 16.dp, vertical = if (isCompact) 6.dp else 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(if (isCompact) 60.dp else 68.dp)
        .shadow(
          elevation = 14.dp,
          shape = RoundedCornerShape(34.dp),
          ambientColor = primary.copy(alpha = 0.18f),
          spotColor = primary.copy(alpha = 0.18f)
        ),
      shape = RoundedCornerShape(34.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 4.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        BottomNavItem(
          tab = MainNavTab.HOME,
          selected = selectedTab == MainNavTab.HOME,
          onClick = { onSelectTab(MainNavTab.HOME) },
          tag = "nav_home"
        )
        BottomNavItem(
          tab = MainNavTab.STATS,
          selected = selectedTab == MainNavTab.STATS,
          onClick = { onSelectTab(MainNavTab.STATS) },
          tag = "nav_stats"
        )

        // Central Prominent Add Button (keeps >= 48dp touch target)
        Box(
          modifier = Modifier
            .size(if (isCompact) 48.dp else 52.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(
              Brush.verticalGradient(
                colors = listOf(secondary, primary)
              )
            )
            .clickable(onClick = onCentralAddClick)
            .testTag("central_add_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add transaction, goal, or budget",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(if (isCompact) 24.dp else 28.dp)
          )
        }

        BottomNavItem(
          tab = MainNavTab.GOALS,
          selected = selectedTab == MainNavTab.GOALS,
          onClick = { onSelectTab(MainNavTab.GOALS) },
          tag = "nav_goals"
        )
        BottomNavItem(
          tab = MainNavTab.PROFILE,
          selected = selectedTab == MainNavTab.PROFILE,
          onClick = { onSelectTab(MainNavTab.PROFILE) },
          tag = "nav_profile"
        )
      }
    }
  }
}

@Composable
private fun BottomNavItem(
  tab: MainNavTab,
  selected: Boolean,
  onClick: () -> Unit,
  tag: String
) {
  val activeColor = MaterialTheme.colorScheme.primary
  val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant
  val icon = when (tab) {
    MainNavTab.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
    MainNavTab.STATS -> if (selected) Icons.Filled.BarChart else Icons.Outlined.BarChart
    MainNavTab.GOALS -> if (selected) Icons.Filled.TrackChanges else Icons.Outlined.TrackChanges
    MainNavTab.PROFILE -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
  }

  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 6.dp)
      .testTag(tag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = tab.label,
      tint = if (selected) activeColor else inactiveColor,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = tab.label,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
      ),
      color = if (selected) activeColor else inactiveColor
    )
  }
}

/**
 * Pill-shaped Segmented Tab Selector used on Screen 4 (This Month / This Year / All Time)
 * and Screen 5 (Overview / Trends / Insights).
 */
@Composable
fun SegmentedPillSelector(
  options: List<String>,
  selectedIndex: Int,
  onSelectIndex: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val primary = MaterialTheme.colorScheme.primary
  val onPrimary = MaterialTheme.colorScheme.onPrimary

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp),
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      options.forEachIndexed { index, label ->
        val isSelected = index == selectedIndex
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) primary else Color.Transparent)
            .clickable { onSelectIndex(index) }
            .testTag("pill_tab_$index"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) onPrimary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

/**
 * Circular Progress Ring used on Screen 2 ("Your Progress" 78% ring).
 */
@Composable
fun CircularProgressRing(
  percentage: Int,
  modifier: Modifier = Modifier,
  size: Dp = 118.dp,
  strokeWidth: Dp = 13.dp
) {
  val primary = MaterialTheme.colorScheme.primary
  val secondary = MaterialTheme.colorScheme.secondary
  val trackColor = MaterialTheme.colorScheme.primaryContainer

  val animatedSweep by animateFloatAsState(
    targetValue = (percentage.coerceIn(0, 100) / 100f) * 360f,
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
    label = "progress_ring"
  )

  Box(
    modifier = modifier
      .size(size)
      .semantics { contentDescription = "Monthly goal progress $percentage percent" },
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
      drawArc(
        color = trackColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
      )
      drawArc(
        brush = Brush.sweepGradient(
          colors = listOf(secondary, primary, JuddlesBrightGreen, secondary)
        ),
        startAngle = -90f,
        sweepAngle = animatedSweep,
        useCenter = false,
        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
      )
    }
    Text(
      text = "$percentage%",
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = if (size < 110.dp) 21.sp else 25.sp
      ),
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

/**
 * Multi-Segment Spending Donut Chart with Center Total and Right-Side Category Legend (Screen 4).
 */
@Composable
fun SpendingDonutChartSection(
  totalSpentFormatted: String,
  categories: List<CategoryBreakdownItem>,
  formatAmount: (Long) -> String,
  modifier: Modifier = Modifier
) {
  val isCompact = LocalCompactMode.current
  val palette = listOf(
    JuddlesCoral,       // Food / Primary Expense
    JuddlesBlue,        // Transport
    JuddlesBrightGreen, // Shopping
    JuddlesTealSoft,    // Bills
    JuddlesAmber,       // Entertainment / Other
    MaterialTheme.colorScheme.primary,
    Color(0xFF8B5CF6),  // Subscriptions / Tech
    Color(0xFFEC4899)   // Personal Care / Gifts
  )

  val donutSize = if (isCompact) 128.dp else 152.dp
  val strokeDp = if (isCompact) 20.dp else 24.dp

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = if (isCompact) 4.dp else 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 18.dp)
  ) {
    Box(
      modifier = Modifier
        .size(donutSize)
        .semantics { contentDescription = "Spending breakdown donut chart, total spent $totalSpentFormatted" },
      contentAlignment = Alignment.Center
    ) {
      val trackColor = MaterialTheme.colorScheme.primaryContainer
      Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        val stroke = strokeDp.toPx()
        if (categories.isEmpty()) {
          drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke)
          )
        } else {
          var currentStartAngle = -90f
          categories.forEachIndexed { idx, item ->
            val sweep = (item.percentage / 100f) * 360f
            drawArc(
              color = palette[idx % palette.size],
              startAngle = currentStartAngle,
              sweepAngle = max(sweep - 2f, 2f),
              useCenter = false,
              style = Stroke(width = stroke, cap = StrokeCap.Butt)
            )
            currentStartAngle += sweep
          }
        }
      }
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = totalSpentFormatted,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = if (isCompact) 18.sp else 21.sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center
        )
        Text(
          text = "Total Spent",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Column(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(if (isCompact) 6.dp else 10.dp)
    ) {
      if (categories.isEmpty()) {
        Text(
          text = "No expenses in this period yet.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      } else {
        categories.take(if (isCompact) 4 else 6).forEachIndexed { idx, item ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(palette[idx % palette.size])
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item.category,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = formatAmount(item.amountMinor),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  }
}

/**
 * Smooth Area Line Chart for "Your Balance Trend" (Screen 5 Reports).
 */
@Composable
fun BalanceTrendLineChart(
  points: List<MonthlyComparisonPoint>,
  modifier: Modifier = Modifier
) {
  val isCompact = LocalCompactMode.current
  val primary = MaterialTheme.colorScheme.primary
  val secondary = MaterialTheme.colorScheme.secondary
  val values = if (points.isEmpty()) listOf(1000L, 1200L, 1500L) else points.map { it.balanceEndMinor }
  val labels = if (points.isEmpty()) listOf("Sep", "Oct", "Nov") else points.map { it.monthLabel }

  Column(modifier = modifier.fillMaxWidth()) {
    Canvas(
      modifier = Modifier
        .fillMaxWidth()
        .height(if (isCompact) 98.dp else 125.dp)
        .semantics { contentDescription = "Balance trend chart across ${labels.joinToString()}" }
    ) {
      val w = size.width
      val h = size.height
      val minVal = (values.minOrNull() ?: 0L).toFloat()
      val maxVal = max((values.maxOrNull() ?: 100L).toFloat(), minVal + 100f)
      val range = max(maxVal - minVal, 1f)

      for (i in 0..2) {
        val y = h * (0.15f + i * 0.32f)
        drawLine(
          color = JuddlesMuted.copy(alpha = 0.14f),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.5f
        )
      }

      val coords = values.mapIndexed { idx, v ->
        val x = if (values.size <= 1) w / 2f else (idx.toFloat() / (values.size - 1)) * (w - 32f) + 16f
        val norm = (v.toFloat() - minVal) / range
        val y = h * 0.80f - norm * (h * 0.62f)
        Offset(x, y)
      }

      if (coords.size >= 2) {
        val linePath = Path().apply {
          moveTo(coords.first().x, coords.first().y)
          for (i in 0 until coords.size - 1) {
            val p0 = coords[i]
            val p1 = coords[i + 1]
            val midX = (p0.x + p1.x) / 2f
            cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
          }
        }
        val fillPath = Path().apply {
          addPath(linePath)
          lineTo(coords.last().x, h * 0.90f)
          lineTo(coords.first().x, h * 0.90f)
          close()
        }
        drawPath(
          path = fillPath,
          brush = Brush.verticalGradient(
            colors = listOf(secondary.copy(alpha = 0.35f), Color.Transparent)
          )
        )
        drawPath(
          path = linePath,
          color = primary,
          style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
      }

      coords.forEach { pt ->
        drawCircle(color = Color.White, center = pt, radius = 8f)
        drawCircle(color = primary, center = pt, radius = 5.5f)
      }
    }

    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      labels.forEach { lbl ->
        Text(
          text = lbl,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

/**
 * Grouped Rounded Bar Chart for "Income vs Expenses" (Screen 5 Reports).
 */
@Composable
fun IncomeVsExpensesBarChart(
  points: List<MonthlyComparisonPoint>,
  modifier: Modifier = Modifier
) {
  val isCompact = LocalCompactMode.current
  val primary = MaterialTheme.colorScheme.primary

  Column(modifier = modifier.fillMaxWidth()) {
    Canvas(
      modifier = Modifier
        .fillMaxWidth()
        .height(if (isCompact) 92.dp else 115.dp)
        .semantics { contentDescription = "Monthly income versus expenses bar chart" }
    ) {
      val w = size.width
      val h = size.height
      val maxVal = max(
        points.maxOfOrNull { max(it.incomeMinor, it.expenseMinor) }?.toFloat() ?: 1000f,
        100f
      )

      for (i in 0..2) {
        val y = h * (0.15f + i * 0.35f)
        drawLine(
          color = JuddlesMuted.copy(alpha = 0.12f),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.5f
        )
      }

      val count = max(points.size, 1)
      val groupWidth = w / count
      val barW = (groupWidth * 0.22f).coerceAtMost(38f)

      points.forEachIndexed { idx, pt ->
        val centerX = groupWidth * idx + groupWidth / 2f
        val incH = ((pt.incomeMinor.toFloat() / maxVal) * (h * 0.78f)).coerceAtLeast(10f)
        val expH = ((pt.expenseMinor.toFloat() / maxVal) * (h * 0.78f)).coerceAtLeast(10f)

        drawRoundRect(
          brush = Brush.verticalGradient(
            colors = listOf(JuddlesBrightGreen, primary)
          ),
          topLeft = Offset(centerX - barW - 4f, h - incH),
          size = Size(barW, incH),
          cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
          brush = Brush.verticalGradient(
            colors = listOf(JuddlesAmber, JuddlesCoral)
          ),
          topLeft = Offset(centerX + 4f, h - expH),
          size = Size(barW, expH),
          cornerRadius = CornerRadius(10f, 10f)
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      points.forEach { pt ->
        Text(
          text = pt.monthLabel,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
