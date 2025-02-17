package com.example.playlistmaker.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.PreferencesManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val sendFeedback = Creator.getSendFeedBack()
    private val openUserAgreement = Creator.getUserAgreement()
    private val shareLink = Creator.getShareLink()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchDarkTheme(checked)
            PreferencesManager.saveThemeStatus(checked)
        }
        binding.settingsBack.setNavigationOnClickListener {
            finish()
        }
        binding.linkApp.setOnClickListener {
            shareLink.action(this@SettingsActivity)
        }
        binding.writeToSupport.setOnClickListener {
            sendFeedback.action(this@SettingsActivity)
        }
        binding.userAgreement.setOnClickListener {
            openUserAgreement.action(this@SettingsActivity)
        }
    }
}