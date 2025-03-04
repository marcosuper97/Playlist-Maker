package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.util.App

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

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
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme

        viewModel.settingsEvent.observe(this) { event ->
            when (event) {
                SettingsEvent.OpenSupport -> {
                    viewModel.sharingInteractor.openSupport(this)
                }

                SettingsEvent.OpenTerms -> {
                    viewModel.sharingInteractor.openTerms(this)
                }

                SettingsEvent.ShareApp -> {
                    viewModel.sharingInteractor.shareApp(this)
                }

                SettingsEvent.SwapTheme -> {
                    if (binding.themeSwitcher.isChecked) {
                        viewModel.themeChangerInteractor.changeTheme(true)
                    }
                    else {
                        viewModel.themeChangerInteractor.changeTheme(false)
                    }
                }
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, _ ->
            viewModel.onChangeThemeClicked()
        }

        binding.settingsBack.setNavigationOnClickListener {
            finish()
        }

        binding.linkApp.setOnClickListener {
            viewModel.onShareAppClicked()
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.onSupportClicked()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.onTermsClicked()
        }
    }
}