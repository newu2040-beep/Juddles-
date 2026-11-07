package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaSans = FontFamily(
  Font(R.font.plus_jakarta_sans, FontWeight.Normal),
  Font(R.font.plus_jakarta_sans, FontWeight.Medium),
  Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
  Font(R.font.plus_jakarta_sans, FontWeight.Bold),
)

val DmSans = FontFamily(
  Font(R.font.dm_sans, FontWeight.Normal),
  Font(R.font.dm_sans, FontWeight.Medium),
  Font(R.font.dm_sans, FontWeight.Bold),
)

val OutfitFontFamily = FontFamily(
  Font(R.font.outfit, FontWeight.Normal),
  Font(R.font.outfit, FontWeight.Medium),
  Font(R.font.outfit, FontWeight.SemiBold),
  Font(R.font.outfit, FontWeight.Bold),
)

val MontserratFontFamily = FontFamily(
  Font(R.font.montserrat, FontWeight.Normal),
  Font(R.font.montserrat, FontWeight.Medium),
  Font(R.font.montserrat, FontWeight.SemiBold),
  Font(R.font.montserrat, FontWeight.Bold),
)

val LoraFontFamily = FontFamily(
  Font(R.font.lora, FontWeight.Normal),
  Font(R.font.lora, FontWeight.Medium),
  Font(R.font.lora, FontWeight.SemiBold),
  Font(R.font.lora, FontWeight.Bold),
)

val JetBrainsMonoFontFamily = FontFamily(
  Font(R.font.jetbrains_mono, FontWeight.Normal),
  Font(R.font.jetbrains_mono, FontWeight.Medium),
  Font(R.font.jetbrains_mono, FontWeight.SemiBold),
  Font(R.font.jetbrains_mono, FontWeight.Bold),
)

data class JuddlesFontOption(
  val key: String,
  val title: String,
  val subtitle: String,
  val badge: String,
  val headingFamily: FontFamily,
  val bodyFamily: FontFamily
)

val JuddlesAvailableFonts = listOf(
  JuddlesFontOption(
    key = "PLUS_JAKARTA",
    title = "Plus Jakarta & DM Sans",
    subtitle = "Signature Juddles geometric & humanist pairing",
    badge = "Default",
    headingFamily = PlusJakartaSans,
    bodyFamily = DmSans
  ),
  JuddlesFontOption(
    key = "SYSTEM",
    title = "System Default Font",
    subtitle = "Native Android OS system typeface (Roboto / Device font)",
    badge = "System",
    headingFamily = FontFamily.Default,
    bodyFamily = FontFamily.Default
  ),
  JuddlesFontOption(
    key = "OUTFIT",
    title = "Outfit Modern",
    subtitle = "Clean geometric fintech display & UI typeface",
    badge = "Inbuilt",
    headingFamily = OutfitFontFamily,
    bodyFamily = OutfitFontFamily
  ),
  JuddlesFontOption(
    key = "MONTSERRAT",
    title = "Montserrat Classic",
    subtitle = "Bold architectural geometric sans-serif",
    badge = "Inbuilt",
    headingFamily = MontserratFontFamily,
    bodyFamily = MontserratFontFamily
  ),
  JuddlesFontOption(
    key = "LORA",
    title = "Lora Editorial Serif",
    subtitle = "Refined editorial serif for wealth & journal style",
    badge = "Inbuilt",
    headingFamily = LoraFontFamily,
    bodyFamily = DmSans
  ),
  JuddlesFontOption(
    key = "JETBRAINS_MONO",
    title = "JetBrains Mono Ledger",
    subtitle = "Precision tabular monospace for numbers & statements",
    badge = "Inbuilt",
    headingFamily = JetBrainsMonoFontFamily,
    bodyFamily = JetBrainsMonoFontFamily
  ),
  JuddlesFontOption(
    key = "SYSTEM_SERIF",
    title = "System Classic Serif",
    subtitle = "Native Android serif typography",
    badge = "System",
    headingFamily = FontFamily.Serif,
    bodyFamily = FontFamily.Serif
  ),
  JuddlesFontOption(
    key = "SYSTEM_MONO",
    title = "System Monospace",
    subtitle = "Native Android fixed-width developer & ledger font",
    badge = "System",
    headingFamily = FontFamily.Monospace,
    bodyFamily = FontFamily.Monospace
  )
)

fun createJuddlesTypography(
  fontKey: String = "PLUS_JAKARTA",
  isCompact: Boolean = false
): Typography {
  val option = JuddlesAvailableFonts.firstOrNull {
    it.key.equals(fontKey, ignoreCase = true)
  } ?: JuddlesAvailableFonts.first()

  val heading = option.headingFamily
  val body = option.bodyFamily
  val scale = if (isCompact) 0.93f else 1.0f

  return Typography(
    displayLarge = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.Bold,
      fontSize = (36f * scale).sp,
      lineHeight = (42f * scale).sp,
      letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.Bold,
      fontSize = (30f * scale).sp,
      lineHeight = (36f * scale).sp,
      letterSpacing = (-0.3).sp
    ),
    headlineLarge = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.Bold,
      fontSize = (26f * scale).sp,
      lineHeight = (32f * scale).sp,
      letterSpacing = (-0.2).sp
    ),
    headlineMedium = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.Bold,
      fontSize = (22f * scale).sp,
      lineHeight = (28f * scale).sp
    ),
    titleLarge = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.Bold,
      fontSize = (19f * scale).sp,
      lineHeight = (25f * scale).sp
    ),
    titleMedium = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.SemiBold,
      fontSize = (16f * scale).sp,
      lineHeight = (22f * scale).sp
    ),
    titleSmall = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.SemiBold,
      fontSize = (14f * scale).sp,
      lineHeight = (20f * scale).sp
    ),
    bodyLarge = TextStyle(
      fontFamily = body,
      fontWeight = FontWeight.Normal,
      fontSize = (16f * scale).sp,
      lineHeight = (24f * scale).sp
    ),
    bodyMedium = TextStyle(
      fontFamily = body,
      fontWeight = FontWeight.Normal,
      fontSize = (14f * scale).sp,
      lineHeight = (20f * scale).sp
    ),
    bodySmall = TextStyle(
      fontFamily = body,
      fontWeight = FontWeight.Normal,
      fontSize = (12f * scale).sp,
      lineHeight = (16f * scale).sp
    ),
    labelLarge = TextStyle(
      fontFamily = heading,
      fontWeight = FontWeight.SemiBold,
      fontSize = (14f * scale).sp,
      lineHeight = (20f * scale).sp
    ),
    labelMedium = TextStyle(
      fontFamily = body,
      fontWeight = FontWeight.Medium,
      fontSize = (12f * scale).sp,
      lineHeight = (16f * scale).sp
    ),
    labelSmall = TextStyle(
      fontFamily = body,
      fontWeight = FontWeight.Medium,
      fontSize = (11f * scale).sp,
      lineHeight = (14f * scale).sp
    )
  )
}

val Typography = createJuddlesTypography("PLUS_JAKARTA", false)
