package com.example.recall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val settings by viewModel.settings.collectAsState()
    val download by viewModel.download.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineMedium)

            Column {
                Text("Cards per day: ${settings.dailyLimit}", style = MaterialTheme.typography.titleMedium)
                Text(
                    "A small daily limit keeps practice short. You can stop at any time.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = settings.dailyLimit.toFloat(),
                    onValueChange = { viewModel.setDailyLimit(it.roundToInt()) },
                    valueRange = 5f..30f,
                    steps = 24
                )
            }

            Column {
                Text(
                    "Text size: ${(settings.textScale * 100).roundToInt()}%",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = settings.textScale,
                    onValueChange = { viewModel.setTextScale(it) },
                    valueRange = 0.85f..1.5f
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .toggleable(
                        value = settings.soundOn,
                        role = Role.Switch,
                        onValueChange = { viewModel.setSoundOn(it) }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sound effects", style = MaterialTheme.typography.titleMedium)
                Switch(checked = settings.soundOn, onCheckedChange = null)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("More questions", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Download extra questions from the internet. This is the only time the app goes online. " +
                            "It asks for questions only and sends none of your study data.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedButton(
                    onClick = viewModel::downloadMoreCards,
                    enabled = !download.busy,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
                ) {
                    Text(if (download.busy) "Downloading…" else "Get more questions")
                }
                download.message?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
                Text(
                    "Questions from the Open Trivia Database (opentdb.com), licensed CC BY-SA 4.0.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            ) { Text("Back") }
        }
    }
}