package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF90CDF4),
    onPrimary = IvyNavyDark,
    primaryContainer = IvyNavyLight,
    onPrimaryContainer = Color.White,
    inversePrimary = IvyNavy,
    secondary = IvyGreenLight,
    onSecondary = Color(0xFF0A2E0F),
    secondaryContainer = IvyGreenDark,
    onSecondaryContainer = Color(0xFFD4EDDA),
    tertiary = IvyGoldLight,
    onTertiary = Color(0xFF422006),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = IvyGoldContainer,
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = IvyUrgentContainer,
    background = Color(0xFF0A1B2D),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF102A43),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF243B53),
    onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceTint = Color(0xFF90CDF4),
    outline = Color(0xFF334E68),
    outlineVariant = Color(0xFF243B53),
    scrim = Color(0xFF000000)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = IvyNavy,
    onPrimary = Color.White,
    primaryContainer = IvyNavyContainer,
    onPrimaryContainer = IvyOnNavyContainer,
    inversePrimary = IvyNavyLight,
    secondary = IvyGreen,
    onSecondary = Color.White,
    secondaryContainer = IvyGreenContainer,
    onSecondaryContainer = IvyOnGreenContainer,
    tertiary = IvyGold,
    onTertiary = Color.White,
    tertiaryContainer = IvyGoldContainer,
    onTertiaryContainer = IvyOnGoldContainer,
    error = IvyUrgentRed,
    onError = Color.White,
    errorContainer = IvyUrgentContainer,
    onErrorContainer = IvyOnUrgentContainer,
    background = IvyBackground,
    onBackground = IvyTextPrimary,
    surface = IvySurface,
    onSurface = IvyTextPrimary,
    surfaceVariant = IvySurfaceVariant,
    onSurfaceVariant = IvyTextSecondary,
    surfaceTint = IvyNavy,
    outline = IvyBorder,
    outlineVariant = Color(0xFFCBD5E1),
    scrim = Color(0xFF000000)
  )

@Composable
fun IvyTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  val ivyBrandColors = remember(darkTheme) {
    if (darkTheme) {
      IvyBrandColors(
        background = Color(0xFF0A1B2D),
        surface = Color(0xFF102A43),
        surfaceVariant = Color(0xFF243B53),
        border = Color(0xFF334E68),
        textPrimary = Color(0xFFF8FAFC),
        textSecondary = Color(0xFFCBD5E1),
        textMuted = Color(0xFF94A3B8)
      )
    } else {
      IvyBrandColors()
    }
  }

  CompositionLocalProvider(
    LocalIvyColors provides ivyBrandColors
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      shapes = IvyShapes,
      content = content
    )
  }
}

object IvyTheme {
  val colorScheme: ColorScheme
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme

  val typography: Typography
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography

  val shapes: Shapes
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes

  val colors: IvyBrandColors
    @Composable
    @ReadOnlyComposable
    get() = LocalIvyColors.current
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  IvyTheme(
    darkTheme = darkTheme,
    dynamicColor = dynamicColor,
    content = content
  )
}

