package com.example.recall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val buttonModifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Recall", style = MaterialTheme.typography.displaySmall)
            Text("Short practice, at your own pace.", style = MaterialTheme.typography.bodyLarge)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (state.remaining == 0) {
                        Text("All done for today!", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            "You studied ${state.studiedToday} cards. Great work! " +
                                    "Come back tomorrow, or raise the daily limit in Settings.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text("${state.toStudy} cards ready today", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            "You have studied ${state.studiedToday} cards today (daily limit: ${state.dailyLimit}).",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Button(onClick = onStudy, enabled = state.toStudy > 0, modifier = buttonModifier) {
                Text("Start practice (all decks)")
            }

            Text("Or pick a deck", style = MaterialTheme.typography.titleMedium)
            deckNames.forEach { deck ->
                val ready = state.dueByDeck[deck] ?: 0
                OutlinedButton(
                    onClick = { onStudyDeck(deck) },
                    enabled = ready > 0 && state.remaining > 0,
                    modifier = buttonModifier
                ) {
                    Text("$deck · $ready ready")
                }
            }

            OutlinedButton(onClick = onStats, modifier = buttonModifier) { Text("My progress") }
            OutlinedButton(onClick = onSettings, modifier = buttonModifier) { Text("Settings") }
        }
    }
}