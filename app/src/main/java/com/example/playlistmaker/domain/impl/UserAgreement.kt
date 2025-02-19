package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.FeedbackLink

class UserAgreement(
) : FeedbackLink {
    override fun action(context: Context) {
        val userAgreementIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
        context.startActivity(userAgreementIntent)
        }
    }