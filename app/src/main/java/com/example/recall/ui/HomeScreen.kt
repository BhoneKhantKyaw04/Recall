package com.example.recall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStudy: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val buttonModifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Recall", style = MaterialTheme.typography.displaySmall)
            Text("Short practice, at your own pace.", style = MaterialTheme.typography.bodyLarge)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${state.toStudy} cards ready today", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "You have studied ${state.studiedToday} of your ${state.dailyLimit} daily cards.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(onClick = onStudy, enabled = state.toStudy > 0, modifier = buttonModifier) {
                Text("Start practice")
            }
            OutlinedButton(onClick = onStats, modifier = buttonModifier) { Text("My progress") }
            OutlinedButton(onClick = onSettings, modifier = buttonModifier) { Text("Settings") }
        }
    }
}