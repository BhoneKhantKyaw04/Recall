package com.example.recall.ui

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val KnewGreen = Color(0xFF1F7A4D)
private val CoralFill = Color(0xFFFFE7E3)
private val CoralBorder = Color(0xFFFF9B8F)

@Composable
fun StudyScreen(viewModel: StudyViewModel, soundOn: Boolean, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val buttonModifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)

    Scaffold(containerColor = colors.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                state.loading -> Text("Loading…", style = MaterialTheme.typography.titleLarge)

                state.finished -> {
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OwlMascot(OwlMood.Cheering, size = 112.dp)
                        Speech("Well done! You are doing great.", Modifier.weight(1f))
                    }
                    Card(
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(3.dp, colors.outlineVariant)
                    ) {
                        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Round complete!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "You reviewed ${state.answered} cards and knew ${state.correct} of them. " +
                                        "Cards you missed will come back soon. See you next time!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Button(onClick = onBack, modifier = buttonModifier) {
                        Text("Back to home", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    }
                }

                else -> {
                    val card = state.currentCard

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Card ${state.answered + 1} of ${state.total}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        TextButton(onClick = onBack, modifier = Modifier.heightIn(min = 48.dp)) {
                            Text("Finish for today", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    LinearProgressIndicator(
                        progress = { state.answered.toFloat() / state.total },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 14.dp),
                        trackColor = colors.outlineVariant
                    )

                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OwlMascot(
                            if (state.showAnswer) OwlMood.Cheering else OwlMood.Listening,
                            size = 96.dp
                        )
                        Speech(
                            if (state.showAnswer) "Great thinking! How did it go?" else "Take your time. No rush!",
                            Modifier.weight(1f)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(3.dp, colors.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .semantics { liveRegion = LiveRegionMode.Polite },
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                DeckAvatar(card?.deck ?: "", size = 44.dp)
                                Text(
                                    card?.deck ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text(
                                card?.question ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (state.showAnswer) colors.onSurfaceVariant else colors.onSurface
                            )
                            if (state.showAnswer) {
                                Surface(color = colors.outlineVariant, modifier = Modifier.fillMaxWidth().heightIn(min = 2.dp)) {}
                                Text("ANSWER", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold, color = colors.primary)
                                Text(
                                    card?.answer ?: "",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    "Think of your answer, then check it.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colors.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (!state.showAnswer) {
                        Button(onClick = viewModel::revealAnswer, modifier = buttonModifier) {
                            Text("Show answer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { if (soundOn) playTone(true); viewModel.answer(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = KnewGreen, contentColor = Color.White),
                                modifier = Modifier.weight(1f).heightIn(min = 60.dp)
                            ) {
                                Text("I knew it", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                            }
                            Button(
                                onClick = { if (soundOn) playTone(false); viewModel.answer(false) },
                                colors = ButtonDefaults.buttonColors(containerColor = CoralFill, contentColor = colors.onSurface),
                                border = BorderStroke(3.dp, CoralBorder),
                                modifier = Modifier.weight(1f).heightIn(min = 60.dp)
                            ) {
                                Text("Not yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Speech(text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.padding(bottom = 20.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun playTone(correct: Boolean) {
    val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 50)
    generator.startTone(
        if (correct) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_PROMPT,
        150
    )
    Handler(Looper.getMainLooper()).postDelayed({ generator.release() }, 400)
}