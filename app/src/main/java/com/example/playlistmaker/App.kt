package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.PreferencesManager

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        PreferencesManager.initFirstLaunchFlag(this)
        PreferencesManager.initThemePreferences(this)
        PreferencesManager.initSearchHistory(this)
        instance = this

        val isFirstLaunch = PreferencesManager.isFirstLaunch()

        if (isFirstLaunch) {
            // Определяем системную тему сегмент if будет только один раз
            val isSystemDarkTheme = isSystemDarkThemeEnabled()
            switchDarkTheme(isSystemDarkTheme)
            // Сохраняем системную тему как пользовательскую настройку
            PreferencesManager.saveThemeStatus(isSystemDarkTheme)
            // Устанавливаем флаг, что первый запуск завершён
            PreferencesManager.setFlag(false)
        } else {
            // Используем сохранённые пользовательские настройки
            val darkThemeEnabled = PreferencesManager.getBoolean()
            switchDarkTheme(darkThemeEnabled)
        }
    }

    fun switchDarkTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun isSystemDarkThemeEnabled(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
    companion object {
        lateinit var instance: App
            private set
    }
}
