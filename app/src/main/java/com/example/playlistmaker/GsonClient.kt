package com.example.playlistmaker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonClient {

private val gson = Gson()

    fun toJson(tracks: ArrayList<Track>): String {
        return gson.toJson(tracks)
    }

    fun fromJson(json: String): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }
}