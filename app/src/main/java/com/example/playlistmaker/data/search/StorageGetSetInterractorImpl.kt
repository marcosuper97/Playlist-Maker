package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.StorageGetSetInterractor
import com.example.playlistmaker.domain.search.StoreGetSetRepository

class StorageGetSetInterractorImpl(private val storeGetSetRepository: StoreGetSetRepository) :
    StorageGetSetInterractor {

    override fun saveTrackInHistory(track: Track) {
        storeGetSetRepository.saveTrackInHistory(track)
    }

    override fun getSearchHistory(): MutableList<Track> {
        return storeGetSetRepository.getSearchHistory()
    }

    override fun getPreferences(): SharedPreferences {
        return storeGetSetRepository.getPreferences()
    }
}