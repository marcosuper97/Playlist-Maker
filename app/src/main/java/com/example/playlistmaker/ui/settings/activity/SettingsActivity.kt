package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel{ parametersOf(this@SettingsActivity) }

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

        viewModel.settingsEvent.observe(this){event ->
            when (event){
                SettingsEvent.OpenSupport -> viewModel.supportEvent()
                SettingsEvent.OpenTerms -> viewModel.termsEvent()
                SettingsEvent.ShareApp -> viewModel.shareAppEvent()
                is SettingsEvent.SwapTheme -> {
                    binding.themeSwitcher.isChecked = event.boolean
                    viewModel.changeThemeEvent(event.boolean)

                }
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, check ->
            viewModel.clickOnChangeTheme(check)
        }

        binding.settingsBack.setNavigationOnClickListener {
            finish()
        }

        binding.linkApp.setOnClickListener {
            viewModel.clickOnShareApp()
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.clickOnSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.clickOnTerms()
        }
    }
}