package com.example.playlistmaker.util

import android.os.Handler
import android.os.Looper

class ClickDebouncer {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}