package com.p4r4d0x.skintker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightThemeColors = lightColors(
    primary = SoftRedPrimary,
    primaryVariant = SoftRedDark,
    onPrimary = Color.White,
    secondary = SoftRedLight,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = SoftRedDark,
    onError = Color.White
)

val DarkThemeColors = darkColors(
    primary = SoftRedPrimary,
    primaryVariant = SoftRedDark,
    onPrimary = Color.Black,
    secondary = Color.Black,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    error = SoftRedDark,
    onError = Color.Black
)

val Colors.snackbarAction: Color
    @Composable
    get() = if (isLight) SoftRedPrimary else SoftRedDark

val Colors.progressIndicatorBackground: Color
    @Composable
    get() = if (isLight) Color.Black.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.24f)

@Composable
fun SkintkerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
