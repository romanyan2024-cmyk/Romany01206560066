package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand Primary & Gradients
val BrandNavyDark = Color(0xFF091E3A)
val BrandNavy = Color(0xFF0C2C55)
val BrandNavyMedium = Color(0xFF134074)
val BrandBlue = Color(0xFF0284C7)
val BrandSky = Color(0xFF38BDF8)
val BrandSkyLight = Color(0xFFE0F2FE)
val BrandSkyVeryLight = Color(0xFFF0F9FF)

// Accents
val AccentGold = Color(0xFFF59E0B)
val AccentGoldLight = Color(0xFFFEF3C7)
val AccentEmerald = Color(0xFF10B981)
val AccentEmeraldLight = Color(0xFFD1FAE5)
val AccentCoral = Color(0xFFF43F5E)
val AccentCoralLight = Color(0xFFFFE4E6)
val AccentPurple = Color(0xFF8B5CF6)
val AccentPurpleLight = Color(0xFFEDE9FE)

// Surfaces & Neutrals
val NeutralSlate900 = Color(0xFF0F172A)
val NeutralSlate800 = Color(0xFF1E293B)
val NeutralSlate700 = Color(0xFF334155)
val NeutralSlate600 = Color(0xFF475569)
val NeutralSlate500 = Color(0xFF64748B)
val NeutralSlate400 = Color(0xFF94A3B8)
val NeutralSlate200 = Color(0xFFE2E8F0)
val NeutralSlate100 = Color(0xFFF1F5F9)
val NeutralSlate50 = Color(0xFFF8FAFC)

// Specific Predefined Gradients
val PrimaryGradient = Brush.horizontalGradient(
  colors = listOf(BrandNavy, BrandBlue)
)

val HeaderBannerGradient = Brush.linearGradient(
  colors = listOf(BrandNavyDark, BrandNavyMedium, BrandBlue)
)

val SkyCardGradient = Brush.verticalGradient(
  colors = listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD))
)

val GoldAccentGradient = Brush.horizontalGradient(
  colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
)

val EmeraldGradient = Brush.horizontalGradient(
  colors = listOf(Color(0xFF10B981), Color(0xFF059669))
)

val AreaMinyaGradient = Brush.linearGradient(
  colors = listOf(Color(0xFF0284C7), Color(0xFF0369A1))
)

val AreaBeniSuefGradient = Brush.linearGradient(
  colors = listOf(Color(0xFF0D9488), Color(0xFF0F766E))
)
