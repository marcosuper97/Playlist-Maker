package com.example.playlistmaker.domain.settings

sealed class SettingsEvent {
    object SwapTheme: SettingsEvent()
    object ShareApp: SettingsEvent()
    object OpenTerms: SettingsEvent()
    object OpenSupport: SettingsEvent()
}