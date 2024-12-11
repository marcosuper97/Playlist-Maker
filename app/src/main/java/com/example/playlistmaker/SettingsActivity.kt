package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shareApp = findViewById<MaterialTextView>(R.id.linkApp)
        val writeToSupport = findViewById<MaterialTextView>(R.id.writeToSupport)
        val userAgreement = findViewById<MaterialTextView>(R.id.userAgreement)
        val settingsBack = findViewById<Toolbar>(R.id.settings_back)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switchThemeSwap)

        switchTheme.setOnClickListener{

        }
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