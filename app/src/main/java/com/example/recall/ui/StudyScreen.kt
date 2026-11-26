package com.example.recall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

@Composable
fun StudyScreen(viewModel: StudyViewModel, soundOn: Boolean, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val buttonModifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                state.loading -> Text("Loading…")

                state.finished -> {
                    Text("Well done!", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "You reviewed ${state.answered} cards and knew ${state.correct} of them. " +
                                "Cards you missed will come back soon. See you next time!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = onBack, modifier = buttonModifier) { Text("Back to home") }
                }

                else -> {
                    val card = state.currentCard
                    Text(
                        "Card ${state.answered + 1} of ${state.total}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    LinearProgressIndicator(
                        progress = { state.answered.toFloat() / state.total },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .semantics { liveRegion = LiveRegionMode.Polite },
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(card?.deck ?: "", style = MaterialTheme.typography.labelLarge)
                            Text(card?.question ?: "", style = MaterialTheme.typography.headlineSmall)
                            if (state.showAnswer) {
                                Text(
                                    "Answer: ${card?.answer}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }

                    if (!state.showAnswer) {
                        Button(onClick = viewModel::revealAnswer, modifier = buttonModifier) {
                            Text("Show answer")
                        }
                    } else {
                        Button(
                            onClick = { if (soundOn) playTone(true); viewModel.answer(true) },
                            modifier = buttonModifier
                        ) {
                            Text("I knew it")
                        }
                        OutlinedButton(
                            onClick = { if (soundOn) playTone(false); viewModel.answer(false) },
                            modifier = buttonModifier
                        ) {
                            Text("Not yet")
                        }
                    }

                    TextButton(onClick = onBack, modifier = Modifier.heightIn(min = 48.dp)) {
                        Text("Finish for today")
                    }
                }

            }
        }
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