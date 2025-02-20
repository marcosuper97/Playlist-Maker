package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.StorageGetSetInterractor
import com.example.playlistmaker.domain.api.TrackHistoryReformater
import com.example.playlistmaker.domain.models.Track

class TrackHistoryReformaterImpl (private val storeGetSetInterractor: StorageGetSetInterractor):TrackHistoryReformater {
    override fun execute(track:Track) {
        val searchHistory = storeGetSetInterractor.getSearchHistory().apply {
            remove(track)
            add(0, track)
            if (size > MAX_COUNT_SEARCH_HISTORY) removeAt(size - 1)
        }
       storeGetSetInterractor.saveTrackInHistory(searchHistory)
    }

    companion object {
        private const val MAX_COUNT_SEARCH_HISTORY = 10
    }
}


