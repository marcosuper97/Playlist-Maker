package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.FeedbackLink

class FeedBackMail(
) : FeedbackLink {
    override fun action(context: Context) {
        val writeToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("gorpishin97@yandex.ru"))
            putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
        }
        context.startActivity(writeToSupportIntent)
    }
}