package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesApiClient {

    private const val BASE_URL_ITUNES = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_ITUNES)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tunesService: ITunesApi = retrofit.create(ITunesApi::class.java)
}