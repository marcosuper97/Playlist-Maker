package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.App
import com.example.playlistmaker.data.PreferencesManager
import com.example.playlistmaker.domain.api.ThemeChanger

class SwapChangerImpl: ThemeChanger {
    private val app = App.instance
    override fun execute(check: Boolean) {
        app.switchDarkTheme(check)
        PreferencesManager.saveThemeStatus(check)
    }
}