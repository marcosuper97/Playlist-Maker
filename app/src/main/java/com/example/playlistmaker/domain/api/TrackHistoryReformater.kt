package com.example.playlistmaker.domain.api


import com.example.playlistmaker.domain.models.Track

interface TrackHistoryReformater {
    fun execute(track: Track)
}