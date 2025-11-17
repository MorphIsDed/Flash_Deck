package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- Custom Color Palettes ---

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF673AB7),    // Deep Purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1C4E9), // Lighter Purple
    onPrimaryContainer = Color(0xFF21005D),

    secondary = Color(0xFF00BCD4),  // Cyan
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2EBF2), // Lighter Cyan
    onSecondaryContainer = Color(0xFF004B50),

    tertiary = Color(0xFF8BC34A),   // Light Green
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDCEDC8),
    onTertiaryContainer = Color(0xFF2F5C00),

    background = Color(0xFFFFFBE6), // Light Cream Background (softer than pure white)
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),     // White Card/Surface Background
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE0E0E0), // Light Grey for subtle distinction
    onSurfaceVariant = Color(0xFF49454F),
    error = Color(0xFFB00020),       // Standard Red for errors
    onError = Color.White,
    errorContainer = Color(0xFFFDD8DF),
    onErrorContainer = Color(0xFF890011),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFC8C5D0),
    scrim = Color(0xFF000000)
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),    // Lighter Purple
    onPrimary = Color(0xFF38006B),
    primaryContainer = Color(0xFF8A5DCF),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFF03DAC5),  // Teal
    onSecondary = Color(0xFF00373C),
    secondaryContainer = Color(0xFF006064),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFFCCFF90),   // Lighter Green
    onTertiary = Color(0xFF1D3E00),
    tertiaryContainer = Color(0xFF6B9A2E),
    onTertiaryContainer = Color(0xFF000000),

    background = Color(0xFF121212), // Dark Grey Background
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1D1D1D),     // Slightly Lighter Dark Grey for cards
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF424242), // Darker Grey for subtle distinction
    onSurfaceVariant = Color(0xFFC2C2C2),
    error = Color(0xFFCF6679),       // Lighter Red for errors
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF8C001C),
    onErrorContainer = Color(0xFFFFFFFF),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF4C4C4C),
    scrim = Color(0xFF000000)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Make sure 'Typography' is defined in the same package
        content = content
    )
}