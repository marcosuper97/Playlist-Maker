package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.models.Track

object PreferencesManager {

    private const val DARK_THEME = "dark_key"
    private const val DARK_PREFERENCES = "dark_preferences"

    private const val HISTORY_PREFERENCES = "history_preferences"
    private const val HISTORY_LIST = "history_list"

    private const val FIRST_LAUNCH = "is_first_launch"
    private const val FIRST_LAUNCH_KEY = "first_launch_key"


    private lateinit var darkThemePreferences: SharedPreferences
    private lateinit var firstLaunchFlag: SharedPreferences
    private lateinit var searchHistoryPreferences: SharedPreferences

    fun initFirstLaunchFlag(context: Context){
        firstLaunchFlag = context.getSharedPreferences(FIRST_LAUNCH, Context.MODE_PRIVATE)
    }

    fun initThemePreferences(context: Context) {
        darkThemePreferences = context.getSharedPreferences(DARK_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun initSearchHistory(context: Context) {
        searchHistoryPreferences =
            context.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun saveSearchHistory(history: MutableList<Track>) {
        val json = GsonClient.listToJson(history)
        searchHistoryPreferences.edit().putString(HISTORY_LIST, json).apply()
    }

    fun saveThemeStatus(value: Boolean) {
        darkThemePreferences.edit().putBoolean(DARK_THEME, value).apply()
    }

    fun getBoolean(): Boolean {
        return darkThemePreferences.getBoolean(DARK_THEME, false)
    }

    fun getSearchHistory(): MutableList<Track> {
        val json = searchHistoryPreferences.getString(HISTORY_LIST, "") ?: ""
        if (json.isNotEmpty()) {
            val trackHistory = GsonClient.arrayFromJson(json)
            return trackHistory
        } else return mutableListOf<Track>()
    }

    fun clearSearchHistory() {
        searchHistoryPreferences.edit().clear().apply()
    }

    fun isFirstLaunch(): Boolean {
        return firstLaunchFlag.getBoolean(FIRST_LAUNCH_KEY, true)
    }

    fun setFlag(status:Boolean){
        firstLaunchFlag.edit().putBoolean(FIRST_LAUNCH_KEY, status).apply()
    }
}