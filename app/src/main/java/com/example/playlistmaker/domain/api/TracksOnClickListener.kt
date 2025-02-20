package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksOnClickListener {
    fun onItemClick(track: Track)
}