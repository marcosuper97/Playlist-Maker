package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface GetPlayerTrack {
    fun get(string:String): Track
}