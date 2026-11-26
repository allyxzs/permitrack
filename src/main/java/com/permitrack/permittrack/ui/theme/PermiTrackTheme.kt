package com.permitrack.permittrack.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowInsets
import androidx.compose.ui.window.WindowInsetsSides
import androidx.compose.ui.window.WindowInsetsAnimationCallback
import androidx.compose.ui.window.WindowInsetsAnimationScope
import androidx.compose.ui.window.WindowInsetsType
import androidx.compose.ui.window.consumedWindowInsets
import androidx.compose.ui.window.ime

@Composable
fun PermiTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

val Typography = Typography(
    h1 = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    h3 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    body1 = TextStyle(fontSize = 16.sp),
    body2 = TextStyle(fontSize = 14.sp)
)

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

val lightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020)
)

val darkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679)
)