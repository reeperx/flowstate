package com.example.flowstate.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppTheme {
    Studio, Club, Midnight, Vinyl, Prism, HighContrast
}

@Immutable
data class FlowStateColors(
    val glassColor: Color = Color.White.copy(alpha = 0.2f),
    val glassBlur: Float = 20f,
    val glassBorder: Color = Color.White.copy(alpha = 0.1f),
    val accentGradient: List<Color> = listOf(Color.Cyan, Color.Magenta)
)

val LocalFlowStateColors = staticCompositionLocalOf { FlowStateColors() }

private val StudioColorScheme = lightColorScheme(
    primary = StudioPrimary,
    secondary = StudioSecondary,
    background = StudioBackground
)

private val ClubColorScheme = darkColorScheme(
    primary = ClubPrimary,
    secondary = ClubSecondary,
    background = ClubBackground
)

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightPrimary,
    secondary = MidnightSecondary,
    background = MidnightBackground
)

private val VinylColorScheme = darkColorScheme(
    primary = VinylPrimary,
    secondary = VinylSecondary,
    background = VinylBackground
)

private val PrismColorScheme = darkColorScheme(
    primary = PrismPrimary,
    secondary = PrismSecondary,
    background = PrismBackground
)

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastPrimary,
    secondary = HighContrastSecondary,
    background = HighContrastBackground,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun FlowStateTheme(
    appTheme: AppTheme = AppTheme.Studio,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.Studio -> StudioColorScheme
        AppTheme.Club -> ClubColorScheme
        AppTheme.Midnight -> MidnightColorScheme
        AppTheme.Vinyl -> VinylColorScheme
        AppTheme.Prism -> PrismColorScheme
        AppTheme.HighContrast -> HighContrastColorScheme
    }

    val flowStateColors = when (appTheme) {
        AppTheme.Studio -> FlowStateColors(
            glassColor = Color.White.copy(alpha = 0.4f),
            accentGradient = listOf(Color(0xFF636E72), Color(0xFF2D3436))
        )
        AppTheme.Club -> FlowStateColors(
            glassColor = Color.White.copy(alpha = 0.1f),
            accentGradient = listOf(Color(0xFFFF00FF), Color(0xFF00FFFF))
        )
        AppTheme.Midnight -> FlowStateColors(
            glassColor = Color.Black.copy(alpha = 0.3f),
            accentGradient = listOf(Color(0xFF1E272E), Color(0xFF485460))
        )
        AppTheme.Vinyl -> FlowStateColors(
            glassColor = Color(0xFFD35400).copy(alpha = 0.2f),
            accentGradient = listOf(Color(0xFFD35400), Color(0xFFF39C12))
        )
        AppTheme.Prism -> FlowStateColors(
            glassColor = Color.White.copy(alpha = 0.2f),
            accentGradient = listOf(Color(0xFF9B59B6), Color(0xFF3498DB), Color(0xFF2ECC71))
        )
        AppTheme.HighContrast -> FlowStateColors(
            glassColor = Color.Transparent,
            glassBorder = Color.White,
            accentGradient = listOf(Color.White, Color.Yellow)
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = appTheme == AppTheme.Studio
    }

    CompositionLocalProvider(LocalFlowStateColors provides flowStateColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
