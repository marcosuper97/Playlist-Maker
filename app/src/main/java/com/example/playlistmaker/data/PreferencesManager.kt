package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.models.Track

object PreferencesManager {

    private const val DARK_THEME = "dark_key"
    private const val DARK_PREFERENCES = "dark_preferences"

    private const val FIRST_LAUNCH = "is_first_launch"
    private const val FIRST_LAUNCH_KEY = "first_launch_key"


    private lateinit var darkThemePreferences: SharedPreferences
    private lateinit var firstLaunchFlag: SharedPreferences

    fun initFirstLaunchFlag(context: Context){
        firstLaunchFlag = context.getSharedPreferences(FIRST_LAUNCH, Context.MODE_PRIVATE)
    }

    fun initThemePreferences(context: Context) {
        darkThemePreferences = context.getSharedPreferences(DARK_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun saveThemeStatus(value: Boolean) {
        darkThemePreferences.edit().putBoolean(DARK_THEME, value).apply()
    }

    fun getBoolean(): Boolean {
        return darkThemePreferences.getBoolean(DARK_THEME, false)
    }

    fun isFirstLaunch(): Boolean {
        return firstLaunchFlag.getBoolean(FIRST_LAUNCH_KEY, true)
    }

    fun setFlag(status:Boolean){
        firstLaunchFlag.edit().putBoolean(FIRST_LAUNCH_KEY, status).apply()
    }
}