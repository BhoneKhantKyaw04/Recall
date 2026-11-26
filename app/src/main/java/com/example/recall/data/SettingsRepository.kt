package com.example.recall.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Settings(
    val dailyLimit: Int = 10,
    val textScale: Float = 1f,
    val soundOn: Boolean = true
)

class SettingsRepository(context: Context) {
    private val prefs = context.getSharedPreferences("recall_settings", Context.MODE_PRIVATE)
    private val _settings = MutableStateFlow(read())
    val settings: StateFlow<Settings> = _settings

    fun setDailyLimit(value: Int) { prefs.edit().putInt("daily_limit", value).apply(); _settings.value = read() }
    fun setTextScale(value: Float) { prefs.edit().putFloat("text_scale", value).apply(); _settings.value = read() }
    fun setSoundOn(value: Boolean) { prefs.edit().putBoolean("sound_on", value).apply(); _settings.value = read() }

    private fun read() = Settings(
        dailyLimit = prefs.getInt("daily_limit", 10),
        textScale = prefs.getFloat("text_scale", 1f),
        soundOn = prefs.getBoolean("sound_on", true)
    )
}