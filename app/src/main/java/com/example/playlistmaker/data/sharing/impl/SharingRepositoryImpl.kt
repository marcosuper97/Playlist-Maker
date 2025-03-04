package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.SharingRepository

class SharingRepositoryImpl() : SharingRepository {
    override fun openSupport(context: Context) {
        val writeToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("gorpishin97@yandex.ru"))
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
        }
        context.startActivity(writeToSupportIntent)
    }

    override fun openTerms(context: Context) {
        val userAgreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
        context.startActivity(userAgreementIntent)
    }

    override fun shareApp(context: Context) {
        val shareApp = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
            putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
        }
        context.startActivity(shareApp)
    }
}
