package com.example.playlistmaker.data.settings.impl


import com.example.playlistmaker.data.settings.ThemeChangerRepository
import com.example.playlistmaker.util.App

class ThemeChangerRepositoryImpl(): ThemeChangerRepository {
    private val app = App.instance

    override fun switchTheme(themeStatus: Boolean) {
        app.switchDarkTheme(themeStatus)
    }
}