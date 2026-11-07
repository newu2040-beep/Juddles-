package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

val JuddlesShapes = Shapes(
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(20.dp),
  large = RoundedCornerShape(24.dp),
  extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun JuddlesTheme(
  themeMode: String = "LIGHT",
  content: @Composable () -> Unit
) {
  val useDark = when (themeMode) {
    "DARK" -> true
    "SYSTEM" -> isSystemInDarkTheme()
    else -> false
  }
  val colorScheme = if (useDark) JuddlesDarkColorScheme else JuddlesLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = JuddlesShapes,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  JuddlesTheme(
    themeMode = if (darkTheme) "DARK" else "LIGHT",
    content = content
  )
}
