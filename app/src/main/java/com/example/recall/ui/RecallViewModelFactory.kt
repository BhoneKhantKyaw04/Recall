package com.example.recall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recall.AppContainer

class RecallViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) ->
            HomeViewModel(container.repository, container.settings)
        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(container.settings, container.repository)
        modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
            ProgressViewModel(container.repository)
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    } as T
}

/** Practice needs to know which deck to study, or null for a mix of everything. */
class StudyViewModelFactory(
    private val container: AppContainer,
    private val deck: String?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StudyViewModel(container.repository, container.settings, deck) as T
}