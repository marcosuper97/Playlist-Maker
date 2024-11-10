package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shareApp = findViewById<FrameLayout>(R.id.shareApp)
        val writeToSupport = findViewById<FrameLayout>(R.id.writeToSupport)
        val userAgreement = findViewById<FrameLayout>(R.id.userAgreement)
        val settingsBack = findViewById<Button>(R.id.settings_back)

        settingsBack.setOnClickListener {
            finish()
        }
        shareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developerYP_page))
            }
            startActivity(shareIntent)
        }
        writeToSupport.setOnClickListener {
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_to_write_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.text_for_support))
            }
            startActivity(writeToSupportIntent)
        }
        userAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(userAgreementIntent)
        }
    }
}