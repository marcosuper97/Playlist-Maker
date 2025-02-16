package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.ClearData


class ClearTrackHistoryImpl(): ClearData {
    override fun perform() {
        PreferencesManager.clearSearchHistory()
    }
}
