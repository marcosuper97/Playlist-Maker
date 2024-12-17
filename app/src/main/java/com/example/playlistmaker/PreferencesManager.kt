package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {

    private const val DARK_THEME = "dark_key"
    private const val DARK_PREFERENCES = "dark_preferences"

    private const val HISTORY_PREFERENCES = "history_preferences"
    private const val HISTORY_LIST = "history_list"

    private lateinit var darkThemePreferences: SharedPreferences
    private lateinit var searchHistoryPreferences: SharedPreferences


    fun initThemePreferences(context: Context) {
        darkThemePreferences = context.getSharedPreferences(DARK_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun initSearchHistory(context: Context) {
        searchHistoryPreferences =
            context.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun saveSearchHistory(history: MutableList<Track>) {
        val json = GsonClient.toJson(history)
        searchHistoryPreferences.edit().putString(HISTORY_LIST, json).apply()
    }

    fun saveThemeStatus(value: Boolean) {
        darkThemePreferences.edit().putBoolean(DARK_THEME, value).apply()
    }

    fun getSearchHistory(): MutableList<Track> {
        val json = searchHistoryPreferences.getString(HISTORY_LIST, "") ?: ""
        if (json.isNotEmpty()) {
            val trackHistory = GsonClient.fromJson(json)
            return trackHistory
        } else return mutableListOf<Track>()
    }

    fun getBoolean(): Boolean {
        return darkThemePreferences.getBoolean(DARK_THEME, false)
    }

    fun clearSearchHistory() {
        searchHistoryPreferences.edit().clear().apply()
    }
}