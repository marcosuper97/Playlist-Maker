package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface SearchTrackInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    fun shutDown()

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}