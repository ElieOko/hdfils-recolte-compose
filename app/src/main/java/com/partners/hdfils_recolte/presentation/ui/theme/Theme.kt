package com.partners.hdfils_recolte.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = BrandBlueLight,
    onPrimaryContainer = BrandBlueDeep,
    secondary = BrandBlueDark,
    onSecondary = Color.White,
    secondaryContainer = BrandBlueSoft,
    onSecondaryContainer = BrandBlueDeep,
    tertiary = BrandBlueDark,
    background = AppBackground,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = BrandBlueLight,
    onSurfaceVariant = InkMuted,
    outline = Line,
    error = Danger,
    onError = Color.White,
)

@Suppress("UNUSED_PARAMETER")
@Composable
fun CasaCleanTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}

@Composable
fun HdfilsrecolteTheme(
    content: @Composable () -> Unit,
) {
    CasaCleanTheme(content)
}
