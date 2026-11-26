package com.example.recall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recall.AppContainer

object Routes {
    const val HOME = "home"
    const val STUDY = "study"
    const val STATS = "stats"
    const val SETTINGS = "settings"
}

@Composable
fun RecallApp(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember { RecallViewModelFactory(container) }

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel(factory = factory),
                onStudy = { navController.navigate(Routes.STUDY) },
                onStats = { navController.navigate(Routes.STATS) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.STUDY) {
            StudyScreen(
                viewModel = viewModel(factory = factory),
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.STATS) { PlaceholderScreen("My progress") { navController.popBackStack() } }
        composable(Routes.SETTINGS) { PlaceholderScreen("Settings") { navController.popBackStack() } }
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text("Coming soon.")
            Button(onClick = onBack, modifier = Modifier.heightIn(min = 56.dp)) { Text("Back") }
        }
    }
}