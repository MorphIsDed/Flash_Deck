package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- TOKYO NIGHT THEME --- //
private val tokyo_night_blue = Color(0xFF7aa2f7)
private val tokyo_night_cyan = Color(0xFF7dcfff)
private val tokyo_night_green = Color(0xFF9ece6a)
private val tokyo_night_red = Color(0xFFf7768e)

private val tokyo_night_dark_bg = Color(0xFF1a1b26)
private val tokyo_night_dark_surface = Color(0xFF24283b)
private val tokyo_night_light_bg = Color(0xFFd5d6db)
private val tokyo_night_light_surface = Color(0xFFe9e9ed)

// --- BLUE TEAL THEME --- //
private val blue_teal_primary = Color(0xFF0D47A1)
private val blue_teal_secondary = Color(0xFF00897B)
private val blue_teal_tertiary = Color(0xFFF57C00)

// --- PURPLE THEME --- //
private val purple_primary = Color(0xFF6A1B9A)
private val purple_secondary = Color(0xFFAB47BC)
private val purple_tertiary = Color(0xFFE91E63)

// --- GREEN THEME --- //
private val green_primary = Color(0xFF2E7D32)
private val green_secondary = Color(0xFF66BB6A)
private val green_tertiary = Color(0xFF9CCC65)

// --- ORANGE THEME --- //
private val orange_primary = Color(0xFFE65100)
private val orange_secondary = Color(0xFFFF6F00)
private val orange_tertiary = Color(0xFFFFB300)

// --- COLOR SCHEME FUNCTIONS --- //

private fun getTokyoNightDarkScheme() = darkColorScheme(
    primary = tokyo_night_blue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3d5a80),
    onPrimaryContainer = Color.White,
    secondary = tokyo_night_cyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3d7a80),
    onSecondaryContainer = Color.White,
    tertiary = tokyo_night_green,
    onTertiary = Color.Black,
    background = tokyo_night_dark_bg,
    onBackground = Color(0xFFc0caf5),
    surface = tokyo_night_dark_surface,
    onSurface = Color(0xFFc0caf5),
    surfaceVariant = Color(0xFF414868),
    onSurfaceVariant = Color(0xFFa9b1d6),
    error = tokyo_night_red,
    onError = Color.Black,
    outline = Color(0xFF565f89)
)

private fun getTokyoNightLightScheme() = lightColorScheme(
    primary = Color(0xFF3d5afe),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFe0e0ff),
    onPrimaryContainer = Color(0xFF00005b),
    secondary = Color(0xFF00bcd4),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFb2ebf2),
    onSecondaryContainer = Color(0xFF004d40),
    tertiary = tokyo_night_green,
    onTertiary = Color.Black,
    background = tokyo_night_light_bg,
    onBackground = Color(0xFF2a2a2e),
    surface = tokyo_night_light_surface,
    onSurface = Color(0xFF2a2a2e),
    surfaceVariant = Color(0xFFc8c8ce),
    onSurfaceVariant = Color(0xFF414146),
    error = tokyo_night_red,
    onError = Color.White,
    outline = Color(0xFF8a8a91)
)

private fun getBlueTealDarkScheme() = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF4DB6AC),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF00695C),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFFFB74D),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getBlueTealLightScheme() = lightColorScheme(
    primary = blue_teal_primary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color.White,
    secondary = blue_teal_secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = blue_teal_tertiary,
    onTertiary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF212121),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getPurpleDarkScheme() = darkColorScheme(
    primary = Color(0xFFBA68C8),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF6A1B9A),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFCE93D8),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF7B1FA2),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFF48FB1),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getPurpleLightScheme() = lightColorScheme(
    primary = purple_primary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A148C),
    secondary = purple_secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF6A1B9A),
    tertiary = purple_tertiary,
    onTertiary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF212121),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getGreenDarkScheme() = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFA5D6A7),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFC5E1A5),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getGreenLightScheme() = lightColorScheme(
    primary = green_primary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = green_secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF2E7D32),
    tertiary = green_tertiary,
    onTertiary = Color.Black,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF212121),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getOrangeDarkScheme() = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFFFCC80),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFBF360C),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFFFE082),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

private fun getOrangeLightScheme() = lightColorScheme(
    primary = orange_primary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFFBF360C),
    secondary = orange_secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary = orange_tertiary,
    onTertiary = Color.Black,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF212121),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    error = Color(0xFFF44336),
    onError = Color.White,
    outline = Color(0xFF757575)
)

fun getColorScheme(theme: String, darkTheme: Boolean, highContrast: Boolean = false): androidx.compose.material3.ColorScheme {
    val baseScheme = when (theme) {
        "blue_teal" -> if (darkTheme) getBlueTealDarkScheme() else getBlueTealLightScheme()
        "purple" -> if (darkTheme) getPurpleDarkScheme() else getPurpleLightScheme()
        "green" -> if (darkTheme) getGreenDarkScheme() else getGreenLightScheme()
        "orange" -> if (darkTheme) getOrangeDarkScheme() else getOrangeLightScheme()
        else -> if (darkTheme) getTokyoNightDarkScheme() else getTokyoNightLightScheme()
    }

    // Apply high contrast if enabled
    return if (highContrast) {
        baseScheme.copy(
            outline = if (darkTheme) Color.White else Color.Black,
            surfaceVariant = if (darkTheme) Color(0xFF616161) else Color(0xFFBDBDBD)
        )
    } else {
        baseScheme
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: String = "tokyo_night",
    fontSize: Int = 0, // 0 = default, 1 = small, 2 = large
    highContrast: Boolean = false,
    largeText: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = getColorScheme(theme, darkTheme, highContrast)
    
    // Adjust typography based on font size and large text setting
    val typography = when {
        largeText || fontSize == 2 -> Typography.copy(
            bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * 1.2f),
            titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * 1.2f)
        )
        fontSize == 1 -> Typography.copy(
            bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * 0.9f),
            titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * 0.9f)
        )
        else -> Typography
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
