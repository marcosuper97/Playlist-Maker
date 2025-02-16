package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.TrackHistory
import com.example.playlistmaker.domain.models.Track

class TrackHistoryImpl : TrackHistory {
    override fun formatHistory(track: Track) {
        val searchHistory = PreferencesManager.getSearchHistory().apply {
            remove(track)
            add(0, track)
            if (size > MAX_COUNT_SEARCH_HISTORY) removeAt(size - 1)
        }
        PreferencesManager.saveSearchHistory(searchHistory)
    }

    companion object {
        private const val MAX_COUNT_SEARCH_HISTORY = 10
    }
}