package com.example.playlistmaker.domain.api

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

interface StoreGetSetRepository {

    fun saveTrackInHistory(history: MutableList<Track>)

    fun getSearchHistory(): MutableList<Track>

    fun getPreferences(): SharedPreferences

}