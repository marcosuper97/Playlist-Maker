package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.FeedbackLink

class ShareApp:FeedbackLink {
    override fun action(context: Context) {
        val shareApp = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
            putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
        }
            context.startActivity(shareApp)
    }
}