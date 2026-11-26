package com.example.recall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recall.data.DownloadResult
import com.example.recall.data.RecallRepository
import com.example.recall.data.Settings
import com.example.recall.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DownloadUiState(val busy: Boolean = false, val message: String? = null)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val recallRepository: RecallRepository
) : ViewModel() {
    val settings: StateFlow<Settings> = repository.settings

    private val _download = MutableStateFlow(DownloadUiState())
    val download: StateFlow<DownloadUiState> = _download.asStateFlow()

    fun setDailyLimit(value: Int) = repository.setDailyLimit(value)
    fun setTextScale(value: Float) = repository.setTextScale(value)
    fun setSoundOn(value: Boolean) = repository.setSoundOn(value)

    fun downloadMoreCards() {
        if (_download.value.busy) return
        viewModelScope.launch {
            _download.value = DownloadUiState(busy = true, message = "Downloading… this takes about 15 seconds.")
            val message = when (val result = recallRepository.downloadMoreCards()) {
                is DownloadResult.Added -> "Added ${result.count} new cards."
                DownloadResult.NothingNew -> "No new questions this time. Try again later."
                is DownloadResult.Failed -> result.reason
            }
            _download.value = DownloadUiState(busy = false, message = message)
        }
    }
}