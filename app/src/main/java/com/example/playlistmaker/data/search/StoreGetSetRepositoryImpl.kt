package com.example.playlistmaker.data.search

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.search.StoreGetSetRepository
import com.example.playlistmaker.domain.models.Track

class StoreGetSetRepositoryImpl(context: Context) : StoreGetSetRepository {

    private val searchHistoryPreferences =
        context.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveTrackInHistory(track: Track) {
        val searchHistory = getSearchHistory().apply {
            remove(track)
            add(0, track)
            if (size > MAX_COUNT_SEARCH_HISTORY) removeAt(size - 1)
        }
        val json = GsonClient.listToJson(searchHistory)
        searchHistoryPreferences.edit().putString(HISTORY_LIST, json).apply()
    }

    override fun getSearchHistory(): MutableList<Track> {
        val json = searchHistoryPreferences.getString(HISTORY_LIST, "") ?: ""
        if (json.isNotEmpty()) {
            val trackHistory = GsonClient.arrayFromJson(json)
            return trackHistory
        } else return mutableListOf<Track>()
    }

    override fun getPreferences(): SharedPreferences {
        return searchHistoryPreferences
    }

    companion object {
        private const val HISTORY_PREFERENCES = "history_preferences"
        private const val HISTORY_LIST = "history_list"
        private const val MAX_COUNT_SEARCH_HISTORY = 10
    }
}