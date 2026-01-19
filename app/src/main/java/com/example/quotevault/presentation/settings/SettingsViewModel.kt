package com.example.quotevault.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.core.ui.theme.AccentColor
import com.example.quotevault.core.ui.theme.FontSizePreference
import com.example.quotevault.core.ui.theme.ThemeMode
import com.example.quotevault.domain.model.UserSettings
import com.example.quotevault.domain.repository.SettingsRepository
import com.example.quotevault.notification.DailyQuoteWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import javax.inject.Inject

data class SettingsUiState(
    val settings: UserSettings = UserSettings.DEFAULT,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            val newSettings = _uiState.value.settings.copy(themeMode = themeMode)
            settingsRepository.updateSettings(newSettings)
        }
    }

    fun setAccentColor(accentColor: AccentColor) {
        viewModelScope.launch {
            val newSettings = _uiState.value.settings.copy(accentColor = accentColor)
            settingsRepository.updateSettings(newSettings)
        }
    }

    fun setFontSize(fontSize: FontSizePreference) {
        viewModelScope.launch {
            val newSettings = _uiState.value.settings.copy(fontSize = fontSize)
            settingsRepository.updateSettings(newSettings)
        }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val newSettings = _uiState.value.settings.copy(notificationEnabled = enabled)
            settingsRepository.updateSettings(newSettings)

            if (enabled) {
                DailyQuoteWorker.schedule(context, newSettings.notificationTime)
            } else {
                DailyQuoteWorker.cancel(context)
            }
        }
    }

    fun setNotificationTime(time: LocalTime) {
        viewModelScope.launch {
            val newSettings = _uiState.value.settings.copy(notificationTime = time)
            settingsRepository.updateSettings(newSettings)

            if (newSettings.notificationEnabled) {
                DailyQuoteWorker.schedule(context, time)
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
