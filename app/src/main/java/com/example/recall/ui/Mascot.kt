package com.example.recall.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val Indigo = Color(0xFF5B5BD6)
private val IndigoDark = Color(0xFF4A4AC0)
private val Belly = Color(0xFFC9C9F7)
private val Amber = Color(0xFFFFB938)
private val Ink = Color(0xFF2B2A4C)

private fun DrawScope.oval(cx: Float, cy: Float, rx: Float, ry: Float, color: Color, s: Float) =
    drawOval(color, Offset((cx - rx) * s, (cy - ry) * s), Size(2 * rx * s, 2 * ry * s))

private fun DrawScope.dot(cx: Float, cy: Float, r: Float, color: Color, s: Float) =
    drawCircle(color, r * s, Offset(cx * s, cy * s))

private fun DrawScope.triangle(
    x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, color: Color, s: Float
) {
    val path = Path().apply {
        moveTo(x1 * s, y1 * s); lineTo(x2 * s, y2 * s); lineTo(x3 * s, y3 * s); close()
    }
    drawPath(path, color)
}

enum class OwlMood { Happy, Listening, Cheering }

@Composable
fun OwlMascot(mood: OwlMood, modifier: Modifier = Modifier, size: Dp = 120.dp) {
    val description = when (mood) {
        OwlMood.Happy -> "Recall the owl, smiling"
        OwlMood.Listening -> "Recall the owl, listening"
        OwlMood.Cheering -> "Recall the owl, cheering with wings up"
    }
    Canvas(modifier = modifier.size(size).semantics { contentDescription = description }) {
        val s = this.size.minDimension / 120f

        oval(46f, 112f, 9f, 4f, Amber, s)
        oval(74f, 112f, 9f, 4f, Amber, s)
        triangle(24f, 30f, 34f, 12f, 48f, 26f, IndigoDark, s)
        triangle(96f, 30f, 86f, 12f, 72f, 26f, IndigoDark, s)

        if (mood == OwlMood.Cheering) {
            rotate(-25f, Offset(14f * s, 52f * s)) { oval(14f, 52f, 9f, 20f, IndigoDark, s) }
            rotate(25f, Offset(106f * s, 52f * s)) { oval(106f, 52f, 9f, 20f, IndigoDark, s) }
        }

        oval(60f, 66f, 42f, 44f, Indigo, s)
        oval(60f, 82f, 26f, 26f, Belly, s)

        val feathers = Path().apply {
            for ((x, y) in listOf(50f to 78f, 62f to 78f, 56f to 90f)) {
                moveTo(x * s, y * s)
                quadraticTo((x + 4f) * s, (y + 4f) * s, (x + 8f) * s, y * s)
            }
        }
        drawPath(feathers, Indigo, style = Stroke(width = 2.5f * s, cap = StrokeCap.Round))

        if (mood != OwlMood.Cheering) {
            oval(22f, 72f, 9f, 20f, IndigoDark, s)
            oval(98f, 72f, 9f, 20f, IndigoDark, s)
        }

        dot(44f, 46f, 15f, Color.White, s)
        dot(76f, 46f, 15f, Color.White, s)

        if (mood == OwlMood.Cheering) {
            val eyes = Path().apply {
                moveTo(33f * s, 49f * s); quadraticTo(44f * s, 34f * s, 55f * s, 49f * s)
                moveTo(65f * s, 49f * s); quadraticTo(76f * s, 34f * s, 87f * s, 49f * s)
            }
            drawPath(eyes, Ink, style = Stroke(width = 5f * s, cap = StrokeCap.Round))
        } else {
            val looking = mood == OwlMood.Listening
            val leftX = if (looking) 44f else 46f
            val rightX = if (looking) 76f else 74f
            val y = if (looking) 44f else 47f
            dot(leftX, y, 7f, Ink, s)
            dot(rightX, y, 7f, Ink, s)
            dot(leftX + 2.5f, y - 3f, 2.4f, Color.White, s)
            dot(rightX + 2.5f, y - 3f, 2.4f, Color.White, s)
        }

        triangle(54f, 56f, 66f, 56f, 60f, 68f, Amber, s)
    }
}

/** Small animal face for each deck: fox = Science, frog = Maths, bear = Geography. */
@Composable
fun DeckAvatar(deck: String, size: Dp = 48.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension / 48f
        when (deck) {
            "Science" -> {
                val orange = Color(0xFFF08A24)
                dot(24f, 24f, 24f, Color(0xFFFFE3C2), s)
                triangle(11f, 14f, 18f, 8f, 21f, 19f, orange, s)
                triangle(37f, 14f, 30f, 8f, 27f, 19f, orange, s)
                oval(24f, 27f, 14f, 13f, orange, s)
                val muzzle = Path().apply {
                    moveTo(12f * s, 30f * s)
                    quadraticTo(24f * s, 44f * s, 36f * s, 30f * s)
                    quadraticTo(24f * s, 34f * s, 12f * s, 30f * s)
                    close()
                }
                drawPath(muzzle, Color.White)
                dot(18f, 24f, 2.2f, Ink, s); dot(30f, 24f, 2.2f, Ink, s)
                oval(24f, 31f, 2.6f, 2f, Ink, s)
            }
            "Maths" -> {
                val green = Color(0xFF3BAA6E)
                dot(24f, 24f, 24f, Color(0xFFD6F2DF), s)
                oval(24f, 29f, 15f, 12f, green, s)
                dot(15f, 18f, 6.5f, green, s); dot(33f, 18f, 6.5f, green, s)
                dot(15f, 18f, 4.2f, Color.White, s); dot(33f, 18f, 4.2f, Color.White, s)
                dot(15.5f, 18.5f, 2f, Ink, s); dot(32.5f, 18.5f, 2f, Ink, s)
                val mouth = Path().apply {
                    moveTo(16f * s, 31f * s)
                    quadraticTo(24f * s, 38f * s, 32f * s, 31f * s)
                }
                drawPath(mouth, Ink, style = Stroke(width = 2.2f * s, cap = StrokeCap.Round))
            }
            "Geography" -> {
                val brown = Color(0xFF8A5A3C)
                val sand = Color(0xFFE9DCCB)
                dot(24f, 24f, 24f, sand, s)
                dot(12f, 14f, 6f, brown, s); dot(36f, 14f, 6f, brown, s)
                dot(24f, 26f, 15f, brown, s)
                oval(24f, 31f, 7f, 5.5f, sand, s)
                dot(18f, 23f, 2.2f, Ink, s); dot(30f, 23f, 2.2f, Ink, s)
                oval(24f, 29.5f, 2.6f, 2f, Ink, s)
            }
            else -> dot(24f, 24f, 24f, Belly, s)
        }
    }
}