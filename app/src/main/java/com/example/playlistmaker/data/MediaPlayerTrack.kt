package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.api.GetPlayerTrack
import com.example.playlistmaker.domain.models.Track

class MediaPlayerTrack:GetPlayerTrack {
    override fun get(string: String): Track {
        return GsonClient.objectFromJson(string)
    }
}