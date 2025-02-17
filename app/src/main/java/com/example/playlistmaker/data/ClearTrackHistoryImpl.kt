package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.ClearTrackHistory


class ClearTrackHistoryImpl(): ClearTrackHistory {
    override fun perform() {
        PreferencesManager.clearSearchHistory()
    }
}
