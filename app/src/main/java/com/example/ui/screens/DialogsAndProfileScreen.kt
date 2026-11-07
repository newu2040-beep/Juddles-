package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FinancialCalculator
import com.example.data.model.FinancialGoal
import com.example.data.model.FinancialTransaction
import com.example.data.model.JuddlesCategoryCatalog
import com.example.data.model.JuddlesCurrencyCatalog
import com.example.data.model.SpendingBudget
import com.example.data.model.UserProfile
import com.example.ui.theme.JuddlesAvailableFonts
import com.example.ui.theme.JuddlesAvailableThemes
import com.example.ui.theme.JuddlesBrightGreen
import com.example.ui.theme.JuddlesCoral
import com.example.ui.theme.LocalCompactMode
import com.example.ui.theme.LocalJuddlesDimensions
import com.example.ui.viewmodel.JuddlesDashboardState
import com.example.util.CustomDateReminder
import com.example.util.ExportFormat
import com.example.util.JuddlesNotificationManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Profile & Personalization Screen:
 * - Account & Opening/Monthly Goal Balances
 * - 12 Curated Themes (Theme Studio)
 * - 8 Inbuilt & System Fonts (Typography Studio)
 * - Custom Date Reminders & Push Notifications
 * - Permissions & Device Access Center (Push Notifications, Files/Folder Access, Vibration)
 * - Multi-Format Export (.PDF, .CSV, .TXT, .MD)
 * - Developer Credit ("Made with love by Rahul Shah") & Version 3.0 (in Profile Section ONLY)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSettingsScreen(
  state: JuddlesDashboardState,
  currentFontMode: String,
  onSelectFontMode: (String) -> Unit,
  reminders: List<CustomDateReminder>,
  onOpenAddReminderDialog: () -> Unit,
  onToggleReminderCompleted: (CustomDateReminder) -> Unit,
  onDeleteReminder: (CustomDateReminder) -> Unit,
  onSendReminderPushNow: (CustomDateReminder) -> Unit,
  hasNotificationPermission: Boolean,
  onRequestNotificationPermissionAndTest: () -> Unit,
  savedExportFolderUri: String,
  onRequestExportFolderAccess: () -> Unit,
  hapticsEnabled: Boolean,
  onToggleHaptics: (Boolean) -> Unit,
  onUpdateProfile: (UserProfile) -> Unit,
  onOpenExportDialog: (ExportFormat?) -> Unit,
  onLoadSampleWorkspace: () -> Unit,
  onClearAllData: () -> Unit,
  onSignOutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showClearConfirm by remember { mutableStateOf(false) }
  var displayNameInput by remember(state.profile.displayName) { mutableStateOf(state.profile.displayName) }
  var openingBalanceInput by remember(state.profile.openingBalanceMinor) {
    mutableStateOf((state.profile.openingBalanceMinor / 100.0).toString())
  }
  var monthlyGoalInput by remember(state.profile.monthlyGoalMinor) {
    mutableStateOf((state.profile.monthlyGoalMinor / 100.0).toString())
  }

  val compact = LocalCompactMode.current || state.profile.compactMode
  val dims = LocalJuddlesDimensions.current
  val primaryColor = MaterialTheme.colorScheme.primary
  val primaryContainer = MaterialTheme.colorScheme.primaryContainer
  var selectedCurrencyRegion by remember { mutableStateOf("All") }
  var currencySearchQuery by remember { mutableStateOf("") }
  var showAllCurrencies by remember { mutableStateOf(false) }

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
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Profile & Settings",
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = primaryContainer
        ) {
          Text(
            text = "v3.0",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = primaryColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }
    }

    // 1. Account & Balances Card
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          )
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(if (compact) 44.dp else 52.dp)
                .clip(CircleShape)
                .background(primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(if (compact) 24.dp else 28.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = state.profile.displayName.ifBlank { "Explorer" },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              if (state.profile.email.isNotBlank()) {
                Text(
                  text = state.profile.email,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(if (compact) 10.dp else 14.dp))
          OutlinedTextField(
            value = displayNameInput,
            onValueChange = { displayNameInput = it },
            label = { Text("Display Name") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = openingBalanceInput,
              onValueChange = { openingBalanceInput = it },
              label = { Text("Opening Balance") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
              value = monthlyGoalInput,
              onValueChange = { monthlyGoalInput = it },
              label = { Text("Monthly Goal") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = {
              val openMinor = ((openingBalanceInput.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong()
              val goalMinor = ((monthlyGoalInput.toDoubleOrNull() ?: 5000.0) * 100.0).roundToLong()
              onUpdateProfile(
                state.profile.copy(
                  displayName = displayNameInput.ifBlank { "Explorer" },
                  openingBalanceMinor = openMinor,
                  monthlyGoalMinor = goalMinor
                )
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Save Profile & Balances")
          }
        }
      }
    }

    // 2. Theme Studio & Layout Density (Compact Mode + 12 Curated Themes)
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          )
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = null,
              tint = primaryColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Theme Studio (12 Modes & Palettes)",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            JuddlesAvailableThemes.forEach { themeOption ->
              val isSelected = state.profile.themeMode.equals(themeOption.key, ignoreCase = true)
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) primaryColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(14.dp)
                  )
                  .clickable {
                    onUpdateProfile(state.profile.copy(themeMode = themeOption.key))
                  }
                  .testTag("theme_option_${themeOption.key}"),
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = if (compact) 7.dp else 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                      Box(
                        modifier = Modifier
                          .size(24.dp)
                          .clip(CircleShape)
                          .background(themeOption.primaryPreview)
                          .border(1.dp, Color.White, CircleShape)
                      )
                      Box(
                        modifier = Modifier
                          .size(24.dp)
                          .clip(CircleShape)
                          .background(themeOption.accentPreview)
                          .border(1.dp, Color.White, CircleShape)
                      )
                      Box(
                        modifier = Modifier
                          .size(24.dp)
                          .clip(CircleShape)
                          .background(themeOption.backgroundPreview)
                          .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                      )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                      ) {
                        Text(
                          text = themeOption.title,
                          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                          color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                          shape = RoundedCornerShape(6.dp),
                          color = primaryContainer.copy(alpha = 0.7f)
                        ) {
                          Text(
                            text = if (themeOption.isDarkPalette) "Dark" else "Light",
                            style = MaterialTheme.typography.labelSmall,
                            color = primaryColor,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                          )
                        }
                      }
                      Text(
                        text = themeOption.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  }
                  if (isSelected) {
                    Box(
                      modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(primaryColor),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected theme",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(15.dp)
                      )
                    }
                  }
                }
              }
            }
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          // Compact Mode Toggle
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                onUpdateProfile(state.profile.copy(compactMode = !state.profile.compactMode))
              }
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Compact Mode",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = if (state.profile.compactMode) {
                  "Active: Condensed paddings, cards & charts across all 5 screens"
                } else {
                  "Reduce vertical spacing & card padding while keeping 48dp touch targets"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = state.profile.compactMode,
              onCheckedChange = { onUpdateProfile(state.profile.copy(compactMode = it)) },
              modifier = Modifier.testTag("compact_mode_switch")
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Hide Sensitive Amounts",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Mask currency values across dashboard and cards",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = state.profile.hideAmounts,
              onCheckedChange = { onUpdateProfile(state.profile.copy(hideAmounts = it)) },
              modifier = Modifier.testTag("hide_amounts_switch")
            )
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

          // Primary Currency Studio (50 Global & Asian Currencies • Featuring Nepal NPR & All Regions)
          val activeCurrencyOpt = JuddlesCurrencyCatalog.findByCode(state.profile.preferredCurrency)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Primary Currency (Nepal, Asia & Global • 50 Currencies)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              val activeDesc = if (activeCurrencyOpt != null) {
                "Active: ${activeCurrencyOpt.flag} ${activeCurrencyOpt.code} (${activeCurrencyOpt.symbol.trim()}) • ${activeCurrencyOpt.name} (${activeCurrencyOpt.country})"
              } else {
                "Active: ${state.profile.preferredCurrency}"
              }
              Text(
                text = activeDesc,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = primaryColor
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = currencySearchQuery,
            onValueChange = { currencySearchQuery = it },
            label = { Text("Search Country or Currency (e.g. Nepal, NPR, India, Japan...)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("currency_search_input")
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Region Filter Chips
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            JuddlesCurrencyCatalog.regions.forEach { region ->
              FilterChip(
                selected = selectedCurrencyRegion == region,
                onClick = { selectedCurrencyRegion = region },
                label = { Text(region) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = primaryColor,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.testTag("currency_region_${region.lowercase().replace(" ", "_")}")
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          val filteredCurrencies = remember(selectedCurrencyRegion, currencySearchQuery, showAllCurrencies) {
            val byRegion = if (selectedCurrencyRegion == "All") {
              JuddlesCurrencyCatalog.currencies
            } else {
              JuddlesCurrencyCatalog.currencies.filter { it.region == selectedCurrencyRegion }
            }
            val q = currencySearchQuery.trim()
            val byQuery = if (q.isEmpty()) {
              byRegion
            } else {
              byRegion.filter {
                it.code.contains(q, ignoreCase = true) ||
                  it.name.contains(q, ignoreCase = true) ||
                  it.country.contains(q, ignoreCase = true) ||
                  it.symbol.contains(q, ignoreCase = true)
              }
            }
            if (selectedCurrencyRegion == "All" && q.isEmpty() && !showAllCurrencies) {
              byQuery.take(20)
            } else {
              byQuery
            }
          }

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            filteredCurrencies.forEach { currOpt ->
              val selected = state.profile.preferredCurrency.equals(currOpt.code, ignoreCase = true)
              FilterChip(
                selected = selected,
                onClick = {
                  onUpdateProfile(state.profile.copy(preferredCurrency = currOpt.code))
                },
                label = {
                  Text("${currOpt.flag} ${currOpt.code} (${currOpt.symbol.trim()}) • ${currOpt.country}")
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = primaryColor,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.testTag("currency_chip_${currOpt.code}")
              )
            }
          }

          if (selectedCurrencyRegion == "All" && currencySearchQuery.isBlank()) {
            TextButton(
              onClick = { showAllCurrencies = !showAllCurrencies },
              modifier = Modifier.testTag("toggle_all_currencies_button")
            ) {
              Text(
                text = if (showAllCurrencies) {
                  "Show Featured 20 Currencies"
                } else {
                  "Show All ${JuddlesCurrencyCatalog.currencies.size} Global & Asian Currencies"
                },
                color = primaryColor,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // 3. Inbuilt & System Fonts Studio (8 Font Families)
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          )
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.FontDownload,
              contentDescription = null,
              tint = primaryColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Inbuilt & System Fonts (8 Typefaces)",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            JuddlesAvailableFonts.forEach { fontOpt ->
              val isSelected = currentFontMode.equals(fontOpt.key, ignoreCase = true)
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) primaryColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(14.dp)
                  )
                  .clickable { onSelectFontMode(fontOpt.key) }
                  .testTag("font_option_${fontOpt.key}"),
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = if (compact) 7.dp else 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Text(
                        text = fontOpt.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                          fontFamily = fontOpt.headingFamily,
                          fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                      )
                      Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = primaryContainer
                      ) {
                        Text(
                          text = fontOpt.badge,
                          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                          color = primaryColor,
                          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                      }
                    }
                    val previewAmt = FinancialCalculator.formatCurrencyMinor(
                      amountMinor = 482075L,
                      currencyCode = state.profile.preferredCurrency,
                      hideAmounts = state.profile.hideAmounts
                    )
                    Text(
                      text = "${fontOpt.subtitle} • $previewAmt",
                      style = MaterialTheme.typography.bodySmall.copy(fontFamily = fontOpt.bodyFamily),
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  if (isSelected) {
                    Box(
                      modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(primaryColor),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected font",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(15.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // 4. Custom Date Reminders & Scheduled Alerts Card
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          ),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Custom Date Reminders (${reminders.size})",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
            TextButton(
              onClick = onOpenAddReminderDialog,
              modifier = Modifier.testTag("add_custom_reminder_button")
            ) {
              Text("+ Add Reminder", color = primaryColor, fontWeight = FontWeight.Bold)
            }
          }

          if (reminders.isEmpty()) {
            Text(
              text = "No custom date reminders yet. Tap '+ Add Reminder' to schedule bill due dates, goal milestones, or tax reminders.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            reminders.forEach { rem ->
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Checkbox(
                    checked = rem.isCompleted,
                    onCheckedChange = { onToggleReminderCompleted(rem) }
                  )
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = rem.title,
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (rem.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                      ),
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    val amtPart = if (rem.amountMinor > 0L) {
                      " • ${FinancialCalculator.formatCurrencyMinor(rem.amountMinor, state.profile.preferredCurrency, state.profile.hideAmounts)}"
                    } else ""
                    Text(
                      text = "${rem.category} • ${rem.targetDateText} at ${rem.targetTimeText}$amtPart",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  IconButton(
                    onClick = { onSendReminderPushNow(rem) },
                    modifier = Modifier.size(36.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.NotificationsActive,
                      contentDescription = "Send push alert now",
                      tint = primaryColor,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                  IconButton(
                    onClick = { onDeleteReminder(rem) },
                    modifier = Modifier.size(36.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = "Delete reminder",
                      tint = JuddlesCoral,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }
            }
          }

          Button(
            onClick = onOpenAddReminderDialog,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.AddAlert, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Schedule Custom Date Reminder")
          }
        }
      }
    }

    // 5. Permissions & Device Access Center (Push Notifications, Files Access, Haptics)
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          ),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = primaryColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Permissions & System Access",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
          }

          // Push Notifications Permission Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Push Notifications",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = if (hasNotificationPermission) JuddlesBrightGreen.copy(alpha = 0.18f) else JuddlesCoral.copy(alpha = 0.18f)
                ) {
                  Text(
                    text = if (hasNotificationPermission) "Allowed" else "Action Needed",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (hasNotificationPermission) primaryColor else JuddlesCoral,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "Allow push alerts for custom date reminders, bills & budget thresholds",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
              onClick = onRequestNotificationPermissionAndTest,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.testTag("allow_push_notifications_button")
            ) {
              Text(
                text = if (hasNotificationPermission) "Test Push" else "Allow Push",
                color = primaryColor,
                fontWeight = FontWeight.Bold
              )
            }
          }

          HorizontalDivider()

          // Files & Folder Access Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Files & Export Storage Access",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = JuddlesBrightGreen.copy(alpha = 0.18f)
                ) {
                  Text(
                    text = if (savedExportFolderUri.isNotBlank()) "Folder Linked" else "SAF Ready",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = primaryColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = if (savedExportFolderUri.isNotBlank()) {
                  "Directory granted: ${savedExportFolderUri.takeLast(32)}"
                } else {
                  "Grant document folder access for saving PDF, CSV, TXT & MD statements"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
              onClick = onRequestExportFolderAccess,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.testTag("grant_files_access_button")
            ) {
              Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(16.dp), tint = primaryColor)
              Spacer(modifier = Modifier.width(4.dp))
              Text("Allow Files", color = primaryColor, fontWeight = FontWeight.Bold)
            }
          }

          HorizontalDivider()

          // Haptic Vibration Permission Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = Icons.Default.Vibration,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Haptic & Vibration Alerts",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "Vibrate on reminder notifications and key actions",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            Switch(
              checked = hapticsEnabled,
              onCheckedChange = onToggleHaptics,
              modifier = Modifier.testTag("haptics_permission_switch")
            )
          }
        }
      }
    }

    // 6. Multi-Format Export Center (.pdf, .csv, .txt, .md), Sample Workspace & Sign Out Card
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(
            horizontal = dims.cardInnerHorizontalPadding,
            vertical = dims.cardInnerVerticalPadding
          ),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "Export Financial Statement (PDF • CSV • TXT • MD)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ExportFormat.entries.forEach { fmt ->
              OutlinedButton(
                onClick = { onOpenExportDialog(fmt) },
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("profile_export_${fmt.extension}_button")
              ) {
                Text(
                  text = ".${fmt.extension.uppercase()}",
                  style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                  color = primaryColor
                )
              }
            }
          }

          Button(
            onClick = { onOpenExportDialog(null) },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("export_csv_profile_button")
          ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Export & Share Report (PDF / CSV / TXT / MD)")
          }

          OutlinedButton(
            onClick = onLoadSampleWorkspace,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Load Labelled Sample Workspace", color = primaryColor)
          }

          OutlinedButton(
            onClick = { showClearConfirm = true },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = JuddlesCoral)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear All Workspace Records", color = JuddlesCoral)
          }

          Button(
            onClick = onSignOutClick,
            colors = ButtonDefaults.buttonColors(containerColor = JuddlesCoral),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("sign_out_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out")
          }
        }
      }
    }

    // 7. Exclusive Developer Credit & Version 3.0 Card (Displayed in Profile Section ONLY)
    item {
      Surface(
        shape = RoundedCornerShape(dims.cardCornerRadius),
        color = primaryContainer.copy(alpha = 0.65f),
        tonalElevation = 2.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profile_developer_credit_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = primaryColor
          ) {
            Text(
              text = "Juddles • Version 3.0",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "Made with ",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = "love",
              tint = Color(0xFFE11D48),
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = " by Rahul Shah",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = primaryColor
            )
          }
          Text(
            text = "Made with love by Rahul Shah • Personal Finance & Goal Suite v3.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }

  if (showClearConfirm) {
    AlertDialog(
      onDismissRequest = { showClearConfirm = false },
      title = { Text("Clear All Records?") },
      text = {
        Text("This permanently deletes all transactions, goals, and budgets in your workspace. This action cannot be undone.")
      },
      confirmButton = {
        Button(
          onClick = {
            showClearConfirm = false
            onClearAllData()
          },
          colors = ButtonDefaults.buttonColors(containerColor = JuddlesCoral)
        ) {
          Text("Delete All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearConfirm = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Custom Date Reminder Creation Dialog with interactive Material 3 DatePickerDialog calendar,
 * preset time selector, category chips, optional amount, and push notification trigger.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomDateReminderDialog(
  currencyCode: String,
  onDismiss: () -> Unit,
  onSaveReminder: (
    title: String,
    category: String,
    targetDateText: String,
    targetTimeText: String,
    amountMinor: Long,
    notifyPush: Boolean
  ) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf(JuddlesNotificationManager.reminderCategories.first()) }
  var targetDateText by remember {
    mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(System.currentTimeMillis() + 86_400_000L)))
  }
  var targetTimeText by remember { mutableStateOf("09:00 AM") }
  var amountText by remember { mutableStateOf("") }
  var notifyPush by remember { mutableStateOf(true) }
  var showCalendarPicker by remember { mutableStateOf(false) }

  val primaryColor = MaterialTheme.colorScheme.primary
  val scrollState = rememberScrollState()
  val timePresets = listOf("08:00 AM", "09:00 AM", "12:00 PM", "06:00 PM", "09:00 PM")

  if (showCalendarPicker) {
    val datePickerState = rememberDatePickerState(
      initialSelectedDateMillis = System.currentTimeMillis() + 86_400_000L
    )
    DatePickerDialog(
      onDismissRequest = { showCalendarPicker = false },
      confirmButton = {
        TextButton(
          onClick = {
            val selectedMillis = datePickerState.selectedDateMillis
            if (selectedMillis != null) {
              targetDateText = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(selectedMillis))
            }
            showCalendarPicker = false
          }
        ) {
          Text("Select Date", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCalendarPicker = false }) {
          Text("Cancel")
        }
      }
    ) {
      DatePicker(state = datePickerState)
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Schedule Custom Date Reminder",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .heightIn(max = 440.dp)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Reminder Title (e.g. Pay Credit Card Bill)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("reminder_title_input")
        )

        Text("Reminder Category", style = MaterialTheme.typography.labelLarge)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          JuddlesNotificationManager.reminderCategories.forEach { cat ->
            FilterChip(
              selected = category == cat,
              onClick = { category = cat },
              label = { Text(cat) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = targetDateText,
            onValueChange = { targetDateText = it },
            label = { Text("Custom Date (YYYY-MM-DD)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          )
          OutlinedButton(
            onClick = { showCalendarPicker = true },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.testTag("open_calendar_picker_button")
          ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = "Pick Date", tint = primaryColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Pick", color = primaryColor)
          }
        }

        Text("Reminder Time", style = MaterialTheme.typography.labelLarge)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          timePresets.forEach { preset ->
            FilterChip(
              selected = targetTimeText == preset,
              onClick = { targetTimeText = preset },
              label = { Text(preset) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("Optional Amount ($currencyCode)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Send Push Notification", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            Text(
              text = "Notify via system push notification & schedule alarm",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(checked = notifyPush, onCheckedChange = { notifyPush = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val amtMinor = ((amountText.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong().coerceAtLeast(0L)
          onSaveReminder(
            title.ifBlank { "Scheduled Financial Reminder" },
            category,
            targetDateText.ifBlank { "2026-11-15" },
            targetTimeText,
            amtMinor,
            notifyPush
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        modifier = Modifier.testTag("save_custom_reminder_button")
      ) {
        Text("Save & Schedule")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

/**
 * Unified Export Report Dialog supporting PDF (.pdf), CSV (.csv), Plain Text (.txt), and Markdown (.md)
 * with both "Save to Device" (SAF CreateDocument) and "Share / Open File" (FileProvider).
 */
@Composable
fun ExportReportDialog(
  initialFormat: ExportFormat = ExportFormat.PDF,
  periodLabel: String,
  onDismiss: () -> Unit,
  onSaveToDevice: (ExportFormat) -> Unit,
  onShareFile: (ExportFormat) -> Unit
) {
  var selectedFormat by remember(initialFormat) { mutableStateOf(initialFormat) }
  val primaryColor = MaterialTheme.colorScheme.primary
  val primaryContainer = MaterialTheme.colorScheme.primaryContainer

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Column {
        Text(
          text = "Export Financial Report",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "Period: $periodLabel • Choose format below",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ExportFormat.entries.forEach { format ->
          val isSelected = selectedFormat == format
          val icon = when (format) {
            ExportFormat.PDF -> Icons.Default.PictureAsPdf
            ExportFormat.CSV -> Icons.Default.TableChart
            ExportFormat.TXT -> Icons.Default.Description
            ExportFormat.MD -> Icons.Default.Code
          }
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) primaryColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
              )
              .clickable { selectedFormat = format }
              .testTag("export_format_card_${format.extension}"),
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) primaryColor else primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = format.label,
                  tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else primaryColor,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = format.label,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = format.subtitle,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
          onClick = {
            onShareFile(selectedFormat)
            onDismiss()
          },
          modifier = Modifier.testTag("export_share_file_button")
        ) {
          Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Share .${selectedFormat.extension.uppercase()}")
        }
        Button(
          onClick = {
            onSaveToDevice(selectedFormat)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
          modifier = Modifier.testTag("export_save_device_button")
        ) {
          Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Save File")
        }
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

/**
 * More Financial Tools Sheet opened from the "More" action tile on the Home Dashboard.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoreToolsDialog(
  compactMode: Boolean,
  currentThemeMode: String,
  currentFontMode: String,
  currentCurrency: String = "USD",
  onDismiss: () -> Unit,
  onToggleCompactMode: () -> Unit,
  onSelectTheme: (String) -> Unit,
  onSelectFont: (String) -> Unit,
  onSelectCurrency: (String) -> Unit = {},
  onOpenCustomReminderClick: () -> Unit,
  onOpenTransferClick: () -> Unit,
  onOpenExportClick: (ExportFormat) -> Unit,
  onOpenReportsClick: () -> Unit
) {
  val primaryColor = MaterialTheme.colorScheme.primary
  val scrollState = rememberScrollState()
  val quickCurrencyCodes = listOf(
    "NPR", "INR", "USD", "EUR", "GBP", "JPY", "SGD", "AED",
    "THB", "MYR", "BDT", "PKR", "LKR", "BTN", "KRW", "CNY", "AUD", "CAD"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "More Financial Tools",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .heightIn(max = 430.dp)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Quick Compact Mode Switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Compact Mode",
              style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "High-density view across all screens",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = compactMode,
            onCheckedChange = { onToggleCompactMode() },
            modifier = Modifier.testTag("more_tools_compact_switch")
          )
        }

        HorizontalDivider()

        // Quick Currency Switch (Nepal, Asia & Global)
        Text(
          text = "Quick Currency Switch (Nepal 🇳🇵, Asia & Global)",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          quickCurrencyCodes.forEach { code ->
            val opt = JuddlesCurrencyCatalog.findByCode(code)
            val labelText = if (opt != null) {
              "${opt.flag} ${opt.code} (${opt.symbol.trim()})"
            } else {
              code
            }
            FilterChip(
              selected = currentCurrency.equals(code, ignoreCase = true),
              onClick = { onSelectCurrency(code) },
              label = { Text(labelText) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        HorizontalDivider()

        // Quick Theme Selector (12 Themes)
        Text(
          text = "Quick Theme Switch (12 Modes)",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          JuddlesAvailableThemes.forEach { t ->
            FilterChip(
              selected = currentThemeMode.equals(t.key, ignoreCase = true),
              onClick = { onSelectTheme(t.key) },
              label = { Text(t.title) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        HorizontalDivider()

        // Quick Font Selector (8 Inbuilt & System Fonts)
        Text(
          text = "Quick Font Switch (Inbuilt & System)",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          JuddlesAvailableFonts.forEach { f ->
            FilterChip(
              selected = currentFontMode.equals(f.key, ignoreCase = true),
              onClick = { onSelectFont(f.key) },
              label = { Text(f.title) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        HorizontalDivider()

        // Direct Export Shortcuts (.PDF, .CSV, .TXT, .MD)
        Text(
          text = "Instant Export (.PDF / .CSV / .TXT / .MD)",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          ExportFormat.entries.forEach { fmt ->
            OutlinedButton(
              onClick = {
                onDismiss()
                onOpenExportClick(fmt)
              },
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(fmt.extension.uppercase(), fontWeight = FontWeight.Bold, color = primaryColor)
            }
          }
        }

        OutlinedButton(
          onClick = {
            onDismiss()
            onOpenCustomReminderClick()
          },
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.AddAlert, contentDescription = null, tint = primaryColor)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Schedule Custom Date Reminder", color = primaryColor, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            onDismiss()
            onOpenTransferClick()
          },
          colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.SwapHoriz, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Record Account Transfer")
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onDismiss()
          onOpenReportsClick()
        }
      ) {
        Text("Open Reports", color = primaryColor, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Close")
      }
    }
  )
}

/**
 * Add or Edit Transaction Dialog (Supports EXPENSE, INCOME, TRANSFER, 26 Categories + Custom Category, and Recurring frequency).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFormDialog(
  initialType: String = "EXPENSE",
  existingTransaction: FinancialTransaction? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSave: (
    existingId: String,
    type: String,
    amountMinor: Long,
    category: String,
    accountName: String,
    targetAccountName: String,
    note: String,
    merchant: String,
    recurring: Boolean,
    frequency: String
  ) -> Unit,
  onDelete: ((String) -> Unit)? = null
) {
  var type by remember { mutableStateOf(existingTransaction?.type ?: initialType) }
  var amountText by remember {
    mutableStateOf(
      if (existingTransaction != null) (existingTransaction.amountMinor / 100.0).toString() else ""
    )
  }
  val defaultCat = existingTransaction?.category
    ?: if (type == "INCOME") "Salary" else if (type == "TRANSFER") "Transfer" else "Food"

  var category by remember { mutableStateOf(defaultCat) }
  var customCategoryInput by remember { mutableStateOf("") }
  var accountName by remember { mutableStateOf(existingTransaction?.accountName ?: "Main Wallet") }
  var targetAccountName by remember { mutableStateOf(existingTransaction?.targetAccountName ?: "Savings Vault") }
  var note by remember { mutableStateOf(existingTransaction?.note ?: "") }
  var merchant by remember { mutableStateOf(existingTransaction?.merchant ?: "") }
  var recurring by remember { mutableStateOf(existingTransaction?.recurring ?: false) }

  val primaryColor = MaterialTheme.colorScheme.primary
  val scrollState = rememberScrollState()

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (existingTransaction == null) "Add Transaction" else "Edit Transaction",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .heightIn(max = 440.dp)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("EXPENSE", "INCOME", "TRANSFER").forEach { t ->
            FilterChip(
              selected = type == t,
              onClick = {
                type = t
                category = when (t) {
                  "INCOME" -> "Salary"
                  "TRANSFER" -> "Transfer"
                  else -> "Food"
                }
              },
              label = { Text(t) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("Amount ($currencyCode)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_amount_input")
        )

        if (type != "TRANSFER") {
          val cats = if (type == "INCOME") {
            JuddlesCategoryCatalog.incomeCategories
          } else {
            JuddlesCategoryCatalog.expenseCategories
          }
          Text(
            text = "Category (${cats.size} presets or custom)",
            style = MaterialTheme.typography.labelLarge
          )
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            cats.forEach { c ->
              FilterChip(
                selected = category == c && customCategoryInput.isBlank(),
                onClick = {
                  category = c
                  customCategoryInput = ""
                },
                label = { Text(c) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = primaryColor,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
              )
            }
          }
          OutlinedTextField(
            value = customCategoryInput,
            onValueChange = { customCategoryInput = it },
            label = { Text("Or Custom Category (Optional)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("tx_custom_category_input")
          )
        } else {
          OutlinedTextField(
            value = targetAccountName,
            onValueChange = { targetAccountName = it },
            label = { Text("To Account") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }

        OutlinedTextField(
          value = merchant,
          onValueChange = { merchant = it },
          label = { Text("Merchant / Payee (Optional)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = note,
          onValueChange = { note = it },
          label = { Text("Note (Optional)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Recurring Monthly", style = MaterialTheme.typography.bodyMedium)
          Switch(checked = recurring, onCheckedChange = { recurring = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val parsed = amountText.toDoubleOrNull() ?: 0.0
          val minor = (parsed * 100.0).roundToLong().coerceAtLeast(1L)
          val resolvedCategory = customCategoryInput.trim().ifBlank { category }
          onSave(
            existingTransaction?.id ?: "",
            type,
            minor,
            resolvedCategory,
            accountName,
            if (type == "TRANSFER") targetAccountName else "",
            note,
            merchant,
            recurring,
            if (recurring) "MONTHLY" else "NONE"
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        modifier = Modifier.testTag("save_transaction_button")
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      Row {
        if (existingTransaction != null && onDelete != null) {
          TextButton(
            onClick = {
              onDelete(existingTransaction.id)
              onDismiss()
            }
          ) {
            Text("Delete", color = JuddlesCoral)
          }
        }
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      }
    }
  )
}

/**
 * Create / Edit / Contribute to Goal Dialog with 10 Icon Categories.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalFormDialog(
  existingGoal: FinancialGoal? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSaveGoal: (
    existingId: String,
    title: String,
    targetMinor: Long,
    currentMinor: Long,
    targetDate: String,
    iconKey: String,
    notes: String
  ) -> Unit,
  onAddContribution: ((FinancialGoal, Long) -> Unit)? = null,
  onDeleteGoal: ((String) -> Unit)? = null
) {
  var title by remember { mutableStateOf(existingGoal?.title ?: "") }
  var targetText by remember {
    mutableStateOf(if (existingGoal != null) (existingGoal.targetMinor / 100.0).toString() else "5000")
  }
  var currentText by remember {
    mutableStateOf(if (existingGoal != null) (existingGoal.currentMinor / 100.0).toString() else "0")
  }
  var contributionText by remember { mutableStateOf("") }
  var targetDate by remember { mutableStateOf(existingGoal?.targetDate ?: "Dec 2027") }
  var iconKey by remember { mutableStateOf(existingGoal?.iconKey ?: "TRAVEL") }
  var notes by remember { mutableStateOf(existingGoal?.notes ?: "") }

  val primaryColor = MaterialTheme.colorScheme.primary
  val primaryContainer = MaterialTheme.colorScheme.primaryContainer
  val icons = JuddlesCategoryCatalog.goalIconKeys
  val scrollState = rememberScrollState()

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (existingGoal == null) "Create New Goal" else "Manage Goal",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .heightIn(max = 440.dp)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (existingGoal != null && onAddContribution != null) {
          Surface(
            color = primaryContainer,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = contributionText,
                onValueChange = { contributionText = it },
                label = { Text("+ Add Contribution ($currencyCode)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f)
              )
              Button(
                onClick = {
                  val addMinor = ((contributionText.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong()
                  if (addMinor > 0L) {
                    onAddContribution(existingGoal, addMinor)
                    onDismiss()
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
              ) {
                Text("Add")
              }
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Goal Title (e.g. Travel the World)") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_title_input")
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = targetText,
            onValueChange = { targetText = it },
            label = { Text("Target ($currencyCode)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = currentText,
            onValueChange = { currentText = it },
            label = { Text("Saved ($currencyCode)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          )
        }

        OutlinedTextField(
          value = targetDate,
          onValueChange = { targetDate = it },
          label = { Text("Target Deadline") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        Text("Icon Category (10 options)", style = MaterialTheme.typography.labelLarge)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          icons.forEach { ic ->
            FilterChip(
              selected = iconKey == ic,
              onClick = { iconKey = ic },
              label = { Text(ic) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val tMinor = ((targetText.toDoubleOrNull() ?: 1000.0) * 100.0).roundToLong().coerceAtLeast(100L)
          val cMinor = ((currentText.toDoubleOrNull() ?: 0.0) * 100.0).roundToLong().coerceAtLeast(0L)
          onSaveGoal(
            existingGoal?.id ?: "",
            title.ifBlank { "Savings Goal" },
            tMinor,
            cMinor,
            targetDate,
            iconKey,
            notes
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        modifier = Modifier.testTag("save_goal_button")
      ) {
        Text("Save Goal")
      }
    },
    dismissButton = {
      Row {
        if (existingGoal != null && onDeleteGoal != null) {
          TextButton(
            onClick = {
              onDeleteGoal(existingGoal.id)
              onDismiss()
            }
          ) {
            Text("Delete", color = JuddlesCoral)
          }
        }
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      }
    }
  )
}

/**
 * Set / Edit Budget Dialog with category scope selector.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BudgetFormDialog(
  existingBudget: SpendingBudget? = null,
  currencyCode: String = "USD",
  onDismiss: () -> Unit,
  onSaveBudget: (
    existingId: String,
    name: String,
    periodType: String,
    category: String,
    limitMinor: Long,
    rolloverEnabled: Boolean,
    alertThresholdPercent: Long
  ) -> Unit
) {
  var name by remember { mutableStateOf(existingBudget?.name ?: "Monthly Budget") }
  var limitText by remember {
    mutableStateOf(
      if (existingBudget != null) (existingBudget.limitMinor / 100.0).toString() else "2000"
    )
  }
  var periodType by remember { mutableStateOf(existingBudget?.periodType ?: "MONTHLY") }
  var budgetCategory by remember { mutableStateOf(existingBudget?.category ?: "ALL") }
  var thresholdText by remember {
    mutableStateOf((existingBudget?.alertThresholdPercent ?: 80L).toString())
  }
  var rollover by remember { mutableStateOf(existingBudget?.rolloverEnabled ?: false) }

  val primaryColor = MaterialTheme.colorScheme.primary
  val scrollState = rememberScrollState()

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Configure Budget",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .heightIn(max = 420.dp)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Budget Name") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = limitText,
          onValueChange = { limitText = it },
          label = { Text("Budget Limit ($currencyCode)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("budget_limit_input")
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("WEEKLY", "MONTHLY", "YEARLY").forEach { p ->
            FilterChip(
              selected = periodType == p,
              onClick = { periodType = p },
              label = { Text(p) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }
        Text("Category Scope", style = MaterialTheme.typography.labelLarge)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          (listOf("ALL") + JuddlesCategoryCatalog.expenseCategories.take(8)).forEach { catScope ->
            FilterChip(
              selected = budgetCategory == catScope,
              onClick = { budgetCategory = catScope },
              label = { Text(catScope) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = primaryColor,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }
        OutlinedTextField(
          value = thresholdText,
          onValueChange = { thresholdText = it },
          label = { Text("Alert Threshold % (e.g. 80)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Rollover Unused Balance", style = MaterialTheme.typography.bodyMedium)
          Switch(checked = rollover, onCheckedChange = { rollover = it })
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val limitMinor = ((limitText.toDoubleOrNull() ?: 2000.0) * 100.0).roundToLong().coerceAtLeast(100L)
          val threshold = (thresholdText.toLongOrNull() ?: 80L).coerceIn(10L, 100L)
          onSaveBudget(
            existingBudget?.id ?: "",
            name.ifBlank { "Monthly Budget" },
            periodType,
            budgetCategory,
            limitMinor,
            rollover,
            threshold
          )
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        modifier = Modifier.testTag("save_budget_button")
      ) {
        Text("Save Budget")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
