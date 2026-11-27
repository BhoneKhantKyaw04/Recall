package com.example.recall.ui

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private val deckNames = listOf("Science", "Maths", "Geography")

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStudy: () -> Unit,
    onStudyDeck: (String) -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val finishedForToday = state.remaining == 0

    Scaffold(containerColor = colors.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column {
                Text("Good to see you!", style = MaterialTheme.typography.bodyLarge, color = colors.onSurfaceVariant)
                Text("Recall", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OwlMascot(OwlMood.Happy, size = 112.dp)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, colors.outlineVariant),
                    modifier = Modifier.weight(1f).padding(bottom = 28.dp)
                ) {
                    Text(
                        if (finishedForToday) "You did it! Come back tomorrow for more."
                        else "Ready for a quick round? I picked ${state.toStudy} cards for you.",
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primary, contentColor = colors.onPrimary)
            ) {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("TODAY", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
                    if (finishedForToday) {
                        Text("All done for today!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "You studied ${state.studiedToday} cards. Great work! " +
                                    "Come back tomorrow, or raise the daily limit in Settings.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text("${state.toStudy} cards ready", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        LinearProgressIndicator(
                            progress = { (state.studiedToday.toFloat() / state.dailyLimit).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth(),
                            color = colors.secondary,
                            trackColor = colors.onPrimary.copy(alpha = 0.25f)
                        )
                        Text(
                            "${state.studiedToday} of ${state.dailyLimit} done. Stop any time.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Button(
                        onClick = onStudy,
                        enabled = state.toStudy > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.secondary,
                            contentColor = colors.onSecondary,
                            disabledContainerColor = colors.onPrimary.copy(alpha = 0.2f),
                            disabledContentColor = colors.onPrimary.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
                    ) {
                        Text("Start practice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Text("Your decks", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                deckNames.forEach { deck ->
                    val ready = state.dueByDeck[deck] ?: 0
                    DeckTile(
                        deck = deck,
                        ready = ready,
                        enabled = ready > 0 && state.remaining > 0,
                        modifier = Modifier.weight(1f),
                        onClick = { onStudyDeck(deck) }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onStats, modifier = Modifier.weight(1f).heightIn(min = 56.dp)) {
                    Text("My progress")
                }
                OutlinedButton(onClick = onSettings, modifier = Modifier.weight(1f).heightIn(min = 56.dp)) {
                    Text("Settings")
                }
            }
        }
    }
}

@Composable
private fun DeckTile(
    deck: String,
    ready: Int,
    enabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White.copy(alpha = 0.6f)
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DeckAvatar(deck)
            Text(
                deck,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("$ready ready", style = MaterialTheme.typography.bodySmall)
        }
    }
}