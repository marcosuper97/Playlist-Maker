package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.util.Creator

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
            SettingsViewModel.getViewModelFactory(Creator.getSharingInteractor(this),Creator.getThemeChangerInteractor())
        )[SettingsViewModel::class.java]

        viewModel.changeTheme.observe(this) { isEnabled ->
            binding.themeSwitcher.isChecked = isEnabled
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, check ->
            viewModel.onChangeThemeClicked(check)
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