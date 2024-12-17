package com.example.playlistmaker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonClient {

    private val gson = Gson()

    fun toJson(tracks: MutableList<Track>): String {
        return gson.toJson(tracks)
    }

    fun fromJson(json: String): MutableList<Track> {
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type)
    }
}