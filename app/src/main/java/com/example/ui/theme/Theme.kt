package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = BrandBlue,
  onPrimary = Color.White,
  primaryContainer = BrandSkyLight,
  onPrimaryContainer = BrandNavyDark,
  secondary = BrandNavyMedium,
  onSecondary = Color.White,
  secondaryContainer = NeutralSlate100,
  onSecondaryContainer = NeutralSlate900,
  tertiary = AccentGold,
  onTertiary = Color.White,
  tertiaryContainer = AccentGoldLight,
  onTertiaryContainer = Color(0xFF78350F),
  background = NeutralSlate50,
  onBackground = NeutralSlate900,
  surface = Color.White,
  onSurface = NeutralSlate900,
  surfaceVariant = NeutralSlate100,
  onSurfaceVariant = NeutralSlate700,
  outline = NeutralSlate200,
  outlineVariant = NeutralSlate200
)

private val DarkColorScheme = darkColorScheme(
  primary = BrandSky,
  onPrimary = BrandNavyDark,
  primaryContainer = BrandNavyMedium,
  onPrimaryContainer = BrandSkyLight,
  secondary = BrandSkyLight,
  onSecondary = BrandNavyDark,
  secondaryContainer = BrandNavyDark,
  onSecondaryContainer = NeutralSlate100,
  tertiary = AccentGold,
  onTertiary = BrandNavyDark,
  background = BrandNavyDark,
  onBackground = NeutralSlate50,
  surface = BrandNavy,
  onSurface = NeutralSlate50,
  surfaceVariant = BrandNavyMedium,
  onSurfaceVariant = NeutralSlate200,
  outline = BrandNavyMedium
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
