package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * CompositionLocals for Compact Mode, Font Mode, and adaptive dimensions across all screens.
 */
val LocalCompactMode = compositionLocalOf { false }
val LocalFontMode = compositionLocalOf { "PLUS_JAKARTA" }

@Immutable
data class JuddlesDimensions(
  val isCompact: Boolean = false,
  val screenHorizontalPadding: Dp = 20.dp,
  val screenTopPadding: Dp = 12.dp,
  val sectionSpacing: Dp = 16.dp,
  val cardInnerHorizontalPadding: Dp = 18.dp,
  val cardInnerVerticalPadding: Dp = 16.dp,
  val cardCornerRadius: Dp = 22.dp,
  val iconBadgeSize: Dp = 48.dp,
  val chartHeight: Dp = 122.dp
)

val StandardJuddlesDimensions = JuddlesDimensions(
  isCompact = false,
  screenHorizontalPadding = 20.dp,
  screenTopPadding = 12.dp,
  sectionSpacing = 16.dp,
  cardInnerHorizontalPadding = 18.dp,
  cardInnerVerticalPadding = 16.dp,
  cardCornerRadius = 22.dp,
  iconBadgeSize = 48.dp,
  chartHeight = 122.dp
)

val CompactJuddlesDimensions = JuddlesDimensions(
  isCompact = true,
  screenHorizontalPadding = 14.dp,
  screenTopPadding = 8.dp,
  sectionSpacing = 10.dp,
  cardInnerHorizontalPadding = 14.dp,
  cardInnerVerticalPadding = 10.dp,
  cardCornerRadius = 16.dp,
  iconBadgeSize = 40.dp,
  chartHeight = 96.dp
)

val LocalJuddlesDimensions = compositionLocalOf { StandardJuddlesDimensions }

data class JuddlesThemeOption(
  val key: String,
  val title: String,
  val subtitle: String,
  val isDarkPalette: Boolean,
  val primaryPreview: Color,
  val backgroundPreview: Color,
  val accentPreview: Color
)

val JuddlesAvailableThemes = listOf(
  JuddlesThemeOption(
    key = "LIGHT",
    title = "Mint & Cream",
    subtitle = "Signature Juddles botanical light",
    isDarkPalette = false,
    primaryPreview = JuddlesEvergreen,
    backgroundPreview = JuddlesWarmCream,
    accentPreview = JuddlesMint
  ),
  JuddlesThemeOption(
    key = "DARK",
    title = "Evergreen Night",
    subtitle = "Deep forest dark palette",
    isDarkPalette = true,
    primaryPreview = JuddlesFreshGreen,
    backgroundPreview = JuddlesDarkBg,
    accentPreview = JuddlesDarkCard
  ),
  JuddlesThemeOption(
    key = "OCEAN",
    title = "Ocean Breeze",
    subtitle = "Crisp coastal teal & aqua",
    isDarkPalette = false,
    primaryPreview = OceanPrimary,
    backgroundPreview = OceanBg,
    accentPreview = OceanSoft
  ),
  JuddlesThemeOption(
    key = "SUNSET",
    title = "Warm Terracotta",
    subtitle = "Earthy clay & amber glow",
    isDarkPalette = false,
    primaryPreview = SunsetPrimary,
    backgroundPreview = SunsetBg,
    accentPreview = SunsetSoft
  ),
  JuddlesThemeOption(
    key = "LAVENDER",
    title = "Royal Botanical",
    subtitle = "Soft plum & lavender mist",
    isDarkPalette = false,
    primaryPreview = LavenderPrimary,
    backgroundPreview = LavenderBg,
    accentPreview = LavenderSoft
  ),
  JuddlesThemeOption(
    key = "MIDNIGHT",
    title = "Midnight Gold",
    subtitle = "Luxury obsidian & gold",
    isDarkPalette = true,
    primaryPreview = MidnightPrimary,
    backgroundPreview = MidnightBg,
    accentPreview = MidnightCard
  ),
  JuddlesThemeOption(
    key = "SAKURA",
    title = "Sakura Blossom",
    subtitle = "Soft rose quartz & cherry petal",
    isDarkPalette = false,
    primaryPreview = SakuraPrimary,
    backgroundPreview = SakuraBg,
    accentPreview = SakuraSoft
  ),
  JuddlesThemeOption(
    key = "CYBERPUNK",
    title = "Neon Cyber",
    subtitle = "Synthwave indigo, cyan & magenta",
    isDarkPalette = true,
    primaryPreview = CyberPrimary,
    backgroundPreview = CyberBg,
    accentPreview = CyberSecondary
  ),
  JuddlesThemeOption(
    key = "COFFEE",
    title = "Espresso Roast",
    subtitle = "Warm latte cream & roasted mocha",
    isDarkPalette = false,
    primaryPreview = CoffeePrimary,
    backgroundPreview = CoffeeBg,
    accentPreview = CoffeeSoft
  ),
  JuddlesThemeOption(
    key = "NORDIC",
    title = "Nordic Frost",
    subtitle = "Crisp glacier slate & arctic blue",
    isDarkPalette = false,
    primaryPreview = NordicPrimary,
    backgroundPreview = NordicBg,
    accentPreview = NordicSoft
  ),
  JuddlesThemeOption(
    key = "AMOLED",
    title = "Pure AMOLED Black",
    subtitle = "True pitch-black OLED battery saver",
    isDarkPalette = true,
    primaryPreview = AmoledPrimary,
    backgroundPreview = AmoledBg,
    accentPreview = AmoledSoft
  ),
  JuddlesThemeOption(
    key = "SYSTEM",
    title = "System Auto",
    subtitle = "Follows device light/dark mode",
    isDarkPalette = false,
    primaryPreview = JuddlesEvergreen,
    backgroundPreview = JuddlesIceGreen,
    accentPreview = JuddlesDarkSurface
  )
)

private val JuddlesLightColorScheme = lightColorScheme(
  primary = JuddlesEvergreen,
  onPrimary = Color.White,
  primaryContainer = JuddlesMint,
  onPrimaryContainer = JuddlesEvergreen,
  secondary = JuddlesFreshGreen,
  onSecondary = Color.White,
  secondaryContainer = JuddlesIceGreen,
  onSecondaryContainer = JuddlesInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesCoralSoft,
  onTertiaryContainer = JuddlesInk,
  background = JuddlesWarmCream,
  onBackground = JuddlesInk,
  surface = JuddlesCardWhite,
  onSurface = JuddlesInk,
  surfaceVariant = JuddlesIceGreen,
  onSurfaceVariant = JuddlesMuted,
  outline = JuddlesMintBorder,
  error = Color(0xFFD64545)
)

private val JuddlesDarkColorScheme = darkColorScheme(
  primary = JuddlesFreshGreen,
  onPrimary = Color.White,
  primaryContainer = JuddlesDarkMintSurface,
  onPrimaryContainer = JuddlesMint,
  secondary = JuddlesBrightGreen,
  onSecondary = Color.White,
  secondaryContainer = JuddlesDarkSurface,
  onSecondaryContainer = JuddlesDarkInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF3E261E),
  onTertiaryContainer = JuddlesCoralSoft,
  background = JuddlesDarkBg,
  onBackground = JuddlesDarkInk,
  surface = JuddlesDarkCard,
  onSurface = JuddlesDarkInk,
  surfaceVariant = JuddlesDarkSurface,
  onSurfaceVariant = JuddlesDarkMuted,
  outline = Color(0xFF2B4D40),
  error = Color(0xFFFF6B6B)
)

private val JuddlesOceanColorScheme = lightColorScheme(
  primary = OceanPrimary,
  onPrimary = Color.White,
  primaryContainer = OceanSoft,
  onPrimaryContainer = OceanPrimary,
  secondary = OceanSecondary,
  onSecondary = Color.White,
  secondaryContainer = OceanSoft,
  onSecondaryContainer = OceanInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesCoralSoft,
  onTertiaryContainer = OceanInk,
  background = OceanBg,
  onBackground = OceanInk,
  surface = OceanCard,
  onSurface = OceanInk,
  surfaceVariant = OceanSoft,
  onSurfaceVariant = OceanMuted,
  outline = Color(0xFFB6DFEE),
  error = Color(0xFFD64545)
)

private val JuddlesSunsetColorScheme = lightColorScheme(
  primary = SunsetPrimary,
  onPrimary = Color.White,
  primaryContainer = SunsetSoft,
  onPrimaryContainer = SunsetPrimary,
  secondary = SunsetSecondary,
  onSecondary = Color.White,
  secondaryContainer = SunsetSoft,
  onSecondaryContainer = SunsetInk,
  tertiary = JuddlesAmber,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesAmberSoft,
  onTertiaryContainer = SunsetInk,
  background = SunsetBg,
  onBackground = SunsetInk,
  surface = SunsetCard,
  onSurface = SunsetInk,
  surfaceVariant = SunsetSoft,
  onSurfaceVariant = SunsetMuted,
  outline = Color(0xFFEBC8B5),
  error = Color(0xFFD64545)
)

private val JuddlesLavenderColorScheme = lightColorScheme(
  primary = LavenderPrimary,
  onPrimary = Color.White,
  primaryContainer = LavenderSoft,
  onPrimaryContainer = LavenderPrimary,
  secondary = LavenderSecondary,
  onSecondary = Color.White,
  secondaryContainer = LavenderSoft,
  onSecondaryContainer = LavenderInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesCoralSoft,
  onTertiaryContainer = LavenderInk,
  background = LavenderBg,
  onBackground = LavenderInk,
  surface = LavenderCard,
  onSurface = LavenderInk,
  surfaceVariant = LavenderSoft,
  onSurfaceVariant = LavenderMuted,
  outline = Color(0xFFD5C6EC),
  error = Color(0xFFD64545)
)

private val JuddlesMidnightColorScheme = darkColorScheme(
  primary = MidnightPrimary,
  onPrimary = Color(0xFF1A1405),
  primaryContainer = MidnightSoft,
  onPrimaryContainer = MidnightPrimary,
  secondary = MidnightSecondary,
  onSecondary = Color.White,
  secondaryContainer = MidnightSurface,
  onSecondaryContainer = MidnightInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF3E261E),
  onTertiaryContainer = JuddlesCoralSoft,
  background = MidnightBg,
  onBackground = MidnightInk,
  surface = MidnightCard,
  onSurface = MidnightInk,
  surfaceVariant = MidnightSurface,
  onSurfaceVariant = MidnightMuted,
  outline = Color(0xFF334155),
  error = Color(0xFFFF6B6B)
)

private val JuddlesSakuraColorScheme = lightColorScheme(
  primary = SakuraPrimary,
  onPrimary = Color.White,
  primaryContainer = SakuraSoft,
  onPrimaryContainer = SakuraPrimary,
  secondary = SakuraSecondary,
  onSecondary = Color.White,
  secondaryContainer = SakuraSoft,
  onSecondaryContainer = SakuraInk,
  tertiary = JuddlesAmber,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesAmberSoft,
  onTertiaryContainer = SakuraInk,
  background = SakuraBg,
  onBackground = SakuraInk,
  surface = SakuraCard,
  onSurface = SakuraInk,
  surfaceVariant = SakuraSoft,
  onSurfaceVariant = SakuraMuted,
  outline = Color(0xFFF9A8D4),
  error = Color(0xFFD64545)
)

private val JuddlesCyberpunkColorScheme = darkColorScheme(
  primary = CyberPrimary,
  onPrimary = Color(0xFF07111A),
  primaryContainer = CyberSoft,
  onPrimaryContainer = CyberPrimary,
  secondary = CyberSecondary,
  onSecondary = Color.White,
  secondaryContainer = CyberSurface,
  onSecondaryContainer = CyberInk,
  tertiary = JuddlesAmber,
  onTertiary = Color.Black,
  tertiaryContainer = CyberSoft,
  onTertiaryContainer = CyberInk,
  background = CyberBg,
  onBackground = CyberInk,
  surface = CyberCard,
  onSurface = CyberInk,
  surfaceVariant = CyberSurface,
  onSurfaceVariant = CyberMuted,
  outline = Color(0xFF4C1D95),
  error = Color(0xFFFF6B6B)
)

private val JuddlesCoffeeColorScheme = lightColorScheme(
  primary = CoffeePrimary,
  onPrimary = Color.White,
  primaryContainer = CoffeeSoft,
  onPrimaryContainer = CoffeePrimary,
  secondary = CoffeeSecondary,
  onSecondary = Color.White,
  secondaryContainer = CoffeeSoft,
  onSecondaryContainer = CoffeeInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesCoralSoft,
  onTertiaryContainer = CoffeeInk,
  background = CoffeeBg,
  onBackground = CoffeeInk,
  surface = CoffeeCard,
  onSurface = CoffeeInk,
  surfaceVariant = CoffeeSoft,
  onSurfaceVariant = CoffeeMuted,
  outline = Color(0xFFD6C2B2),
  error = Color(0xFFD64545)
)

private val JuddlesNordicColorScheme = lightColorScheme(
  primary = NordicPrimary,
  onPrimary = Color.White,
  primaryContainer = NordicSoft,
  onPrimaryContainer = NordicPrimary,
  secondary = NordicSecondary,
  onSecondary = Color.White,
  secondaryContainer = NordicSoft,
  onSecondaryContainer = NordicInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = JuddlesCoralSoft,
  onTertiaryContainer = NordicInk,
  background = NordicBg,
  onBackground = NordicInk,
  surface = NordicCard,
  onSurface = NordicInk,
  surfaceVariant = NordicSoft,
  onSurfaceVariant = NordicMuted,
  outline = Color(0xFFBCCCDC),
  error = Color(0xFFD64545)
)

private val JuddlesAmoledColorScheme = darkColorScheme(
  primary = AmoledPrimary,
  onPrimary = Color(0xFF042217),
  primaryContainer = AmoledSoft,
  onPrimaryContainer = AmoledPrimary,
  secondary = AmoledSecondary,
  onSecondary = Color.White,
  secondaryContainer = AmoledSurface,
  onSecondaryContainer = AmoledInk,
  tertiary = JuddlesCoral,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF3E261E),
  onTertiaryContainer = JuddlesCoralSoft,
  background = AmoledBg,
  onBackground = AmoledInk,
  surface = AmoledCard,
  onSurface = AmoledInk,
  surfaceVariant = AmoledSurface,
  onSurfaceVariant = AmoledMuted,
  outline = Color(0xFF1F2E28),
  error = Color(0xFFFF6B6B)
)

val JuddlesShapes = Shapes(
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(20.dp),
  large = RoundedCornerShape(24.dp),
  extraLarge = RoundedCornerShape(32.dp)
)

val JuddlesCompactShapes = Shapes(
  small = RoundedCornerShape(10.dp),
  medium = RoundedCornerShape(16.dp),
  large = RoundedCornerShape(18.dp),
  extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun JuddlesTheme(
  themeMode: String = "LIGHT",
  compactMode: Boolean = false,
  fontMode: String = "PLUS_JAKARTA",
  content: @Composable () -> Unit
) {
  val systemDark = isSystemInDarkTheme()
  val colorScheme = when (themeMode.uppercase()) {
    "DARK" -> JuddlesDarkColorScheme
    "OCEAN" -> JuddlesOceanColorScheme
    "SUNSET" -> JuddlesSunsetColorScheme
    "LAVENDER" -> JuddlesLavenderColorScheme
    "MIDNIGHT" -> JuddlesMidnightColorScheme
    "SAKURA" -> JuddlesSakuraColorScheme
    "CYBERPUNK" -> JuddlesCyberpunkColorScheme
    "COFFEE" -> JuddlesCoffeeColorScheme
    "NORDIC" -> JuddlesNordicColorScheme
    "AMOLED" -> JuddlesAmoledColorScheme
    "SYSTEM" -> if (systemDark) JuddlesDarkColorScheme else JuddlesLightColorScheme
    else -> JuddlesLightColorScheme
  }

  val dimensions = if (compactMode) CompactJuddlesDimensions else StandardJuddlesDimensions
  val shapes = if (compactMode) JuddlesCompactShapes else JuddlesShapes
  val activeTypography = createJuddlesTypography(fontKey = fontMode, isCompact = compactMode)

  CompositionLocalProvider(
    LocalCompactMode provides compactMode,
    LocalFontMode provides fontMode,
    LocalJuddlesDimensions provides dimensions
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = activeTypography,
      shapes = shapes,
      content = content
    )
  }
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  JuddlesTheme(
    themeMode = if (darkTheme) "DARK" else "LIGHT",
    compactMode = false,
    fontMode = "PLUS_JAKARTA",
    content = content
  )
}
