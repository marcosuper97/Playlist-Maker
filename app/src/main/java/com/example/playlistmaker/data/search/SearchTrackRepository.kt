package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.models.Track

interface SearchTrackRepository {
    fun searchTracks(expression: String): List<Track>
}