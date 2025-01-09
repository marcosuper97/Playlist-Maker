package com.example.playlistmaker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonClient {

    private val gson = Gson()

    fun listToJson(tracks: MutableList<Track>): String {
        return gson.toJson(tracks)
    }

    fun objectToJson(track:Track):String{
        return gson.toJson(track)
    }

    fun arrayFromJson(json: String): MutableList<Track> {
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    fun objectFromJson(json: String): Track {
        val type = object : TypeToken<Track>() {}.type
        return gson.fromJson(json, type)
    }
}