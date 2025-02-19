package com.example.playlistmaker.domain.api

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

interface TracksOnClickListener {
    fun onItemClick(track: Track)
}