package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// IVY Childcare Services Brand Colors
// Deep Royal Navy - Primary brand color
val IvyNavy = Color(0xFF102A43)
val IvyNavyDark = Color(0xFF0A1B2D)
val IvyNavyLight = Color(0xFF243B53)
val IvyNavyContainer = Color(0xFFE2E8F0)
val IvyOnNavyContainer = Color(0xFF102A43)

// Garden Green - Secondary / Action color
val IvyGreen = Color(0xFF2E7D32)
val IvyGreenDark = Color(0xFF1B5E20)
val IvyGreenLight = Color(0xFF4CAF50)
val IvyGreenContainer = Color(0xFFE8F5E9)
val IvyOnGreenContainer = Color(0xFF1B5E20)

// Warm Gold - Accent / Highlight / Celebrations
val IvyGold = Color(0xFFD99B26)
val IvyGoldDark = Color(0xFFB7791F)
val IvyGoldLight = Color(0xFFF6E05E)
val IvyGoldContainer = Color(0xFFFEF3C7)
val IvyOnGoldContainer = Color(0xFF78350F)

// Neutrals & Surfaces
val IvyBackground = Color(0xFFF8FAFC)
val IvySurface = Color(0xFFFFFFFF)
val IvySurfaceVariant = Color(0xFFF1F5F9)
val IvyBorder = Color(0xFFE2E8F0)
val IvyTextPrimary = Color(0xFF0F172A)
val IvyTextSecondary = Color(0xFF475569)
val IvyTextMuted = Color(0xFF94A3B8)

// Status & Priority Colors
val IvyUrgentRed = Color(0xFFDC2626)
val IvyUrgentContainer = Color(0xFFFEE2E2)
val IvyOnUrgentContainer = Color(0xFF991B1B)

val IvyImportantOrange = Color(0xFFEA580C)
val IvyImportantContainer = Color(0xFFFFEDD5)
val IvyOnImportantContainer = Color(0xFF9A3412)

@androidx.compose.runtime.Immutable
data class IvyBrandColors(
  val navy: Color = IvyNavy,
  val navyDark: Color = IvyNavyDark,
  val navyLight: Color = IvyNavyLight,
  val navyContainer: Color = IvyNavyContainer,
  val onNavyContainer: Color = IvyOnNavyContainer,
  val green: Color = IvyGreen,
  val greenDark: Color = IvyGreenDark,
  val greenLight: Color = IvyGreenLight,
  val greenContainer: Color = IvyGreenContainer,
  val onGreenContainer: Color = IvyOnGreenContainer,
  val gold: Color = IvyGold,
  val goldDark: Color = IvyGoldDark,
  val goldLight: Color = IvyGoldLight,
  val goldContainer: Color = IvyGoldContainer,
  val onGoldContainer: Color = IvyOnGoldContainer,
  val urgentRed: Color = IvyUrgentRed,
  val urgentContainer: Color = IvyUrgentContainer,
  val onUrgentContainer: Color = IvyOnUrgentContainer,
  val importantOrange: Color = IvyImportantOrange,
  val importantContainer: Color = IvyImportantContainer,
  val onImportantContainer: Color = IvyOnImportantContainer,
  val background: Color = IvyBackground,
  val surface: Color = IvySurface,
  val surfaceVariant: Color = IvySurfaceVariant,
  val border: Color = IvyBorder,
  val textPrimary: Color = IvyTextPrimary,
  val textSecondary: Color = IvyTextSecondary,
  val textMuted: Color = IvyTextMuted
)

val LocalIvyColors = androidx.compose.runtime.staticCompositionLocalOf { IvyBrandColors() }

/**
 * Standard text field colors ensuring typed text, labels, and placeholders
 * are crisp, highly readable, and always clearly visible on light surfaces regardless
 * of system dark/light theme setting.
 */
@androidx.compose.runtime.Composable
fun ivyTextFieldColors(
  containerColor: Color = Color.White,
  textColor: Color = IvyTextPrimary,
  focusedBorder: Color = IvyNavy,
  unfocusedBorder: Color = IvyBorder
) = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
  focusedTextColor = textColor,
  unfocusedTextColor = textColor,
  focusedContainerColor = containerColor,
  unfocusedContainerColor = containerColor,
  disabledContainerColor = containerColor.copy(alpha = 0.6f),
  cursorColor = IvyNavy,
  focusedBorderColor = focusedBorder,
  unfocusedBorderColor = unfocusedBorder,
  focusedLabelColor = IvyNavy,
  unfocusedLabelColor = IvyTextSecondary,
  focusedPlaceholderColor = IvyTextMuted,
  unfocusedPlaceholderColor = IvyTextMuted,
  focusedLeadingIconColor = IvyNavy,
  unfocusedLeadingIconColor = IvyNavy,
  focusedTrailingIconColor = IvyTextSecondary,
  unfocusedTrailingIconColor = IvyTextMuted
)


