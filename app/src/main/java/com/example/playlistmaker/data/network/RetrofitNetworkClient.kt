package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val urlITunes = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(urlITunes)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tunesService: ITunesApi = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = tunesService.search(dto.expression).execute()

            val body = resp.body() ?: TrackSearchResponse(emptyList())

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}