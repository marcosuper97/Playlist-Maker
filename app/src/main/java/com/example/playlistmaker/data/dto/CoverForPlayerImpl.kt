package com.example.playlistmaker.data.dto

import com.example.playlistmaker.data.network.CoverForPlayer

class CoverForPlayerImpl:CoverForPlayer {
    override fun editFormat(string: String): String {
        return string.replaceAfterLast('/', FORMAT_SIZE)
    }
    companion object{
        private const val FORMAT_SIZE = "512x512bb.jpg"
    }
}