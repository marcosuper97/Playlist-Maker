package com.example.playlistmaker.domain.api

import android.content.Context

interface AppNavigation {
    fun settings(context: Context) {}
    fun library(context: Context) {}
    fun search(context: Context) {}
}