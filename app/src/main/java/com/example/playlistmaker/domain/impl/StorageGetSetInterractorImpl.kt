package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.StorageGetSetInterractor
import com.example.playlistmaker.domain.api.StoreGetSetRepository
import com.example.playlistmaker.domain.models.Track

class StorageGetSetInterractorImpl(private val storeGetSetRepository: StoreGetSetRepository) :
    StorageGetSetInterractor {

    override fun saveTrackInHistory(history: MutableList<Track>) {
        storeGetSetRepository.saveTrackInHistory(history)
    }

    override fun getSearchHistory(): MutableList<Track> {
        return storeGetSetRepository.getSearchHistory()
    }

    override fun getPreferences(): SharedPreferences {
        return storeGetSetRepository.getPreferences()
    }
}