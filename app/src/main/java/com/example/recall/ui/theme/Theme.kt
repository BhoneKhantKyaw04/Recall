package com.example.recall.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val RecallColors = lightColorScheme(
    primary = Color(0xFF5B5BD6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3E3FF),
    onPrimaryContainer = Color(0xFF2B2A4C),
    secondary = Color(0xFFFFB938),
    onSecondary = Color(0xFF2B2A4C),
    tertiary = Color(0xFFFFB938),
    onTertiary = Color(0xFF2B2A4C),
    background = Color(0xFFFFF7E8),
    onBackground = Color(0xFF2B2A4C),
    surface = Color(0xFFFFF7E8),
    onSurface = Color(0xFF2B2A4C),
    surfaceVariant = Color(0xFFF0E3CC),
    onSurfaceVariant = Color(0xFF5C5B7A),
    outline = Color(0xFF8A8AA8),
    outlineVariant = Color(0xFFF0E3CC),
    surfaceContainerHighest = Color.White
)

private val RecallShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun RecallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RecallColors,
        typography = Typography,
        shapes = RecallShapes,
        content = content
    )
}