package com.example.recall.ui

import androidx.lifecycle.ViewModel
import com.example.recall.data.Settings
import com.example.recall.data.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<Settings> = repository.settings

    fun setDailyLimit(value: Int) = repository.setDailyLimit(value)
    fun setTextScale(value: Float) = repository.setTextScale(value)
    fun setSoundOn(value: Boolean) = repository.setSoundOn(value)
}