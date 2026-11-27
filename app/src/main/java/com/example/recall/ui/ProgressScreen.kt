package com.example.recall.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProgressScreen(viewModel: ProgressViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("My progress", style = MaterialTheme.typography.headlineMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Cards in total", "${state.totalCards}", Modifier.weight(1f))
                StatCard("Known well", "${state.knownCards}", Modifier.weight(1f))
                StatCard("Right today", state.accuracyToday?.let { "$it%" } ?: "-", Modifier.weight(1f))
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cards practised this week", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Rest days are fine. Come back when you like.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    WeekChart(state.week)
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            ) { Text("Back") }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun WeekChart(week: List<DayBar>) {
    val maxCount = maxOf(1, week.maxOfOrNull { it.count } ?: 1)
    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp).padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        week.forEach { bar ->
            Column(
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "${bar.label}: ${bar.count} cards"
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height((100 * bar.count / maxCount).coerceAtLeast(4).dp)
                        .background(
                            color = if (bar.isToday) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                )
                Text(bar.label, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}