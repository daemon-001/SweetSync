package com.sweetsync.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AppPrimary,
    onPrimary = OnPrimary,
    primaryContainer = AppPrimaryContainer,
    onPrimaryContainer = AppPrimaryDark,
    secondary = WeightTeal,
    onSecondary = OnPrimary,
    tertiary = SugarOrange,
    onTertiary = OnPrimary,
    background = ScreenBackground,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    outline = CardStrokeDarker,
    outlineVariant = CardStroke
)

@Composable
fun SweetSyncTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = ScreenBackground.toArgb()
            window.navigationBarColor = CardSurface.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
