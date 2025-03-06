package com.example.playlistmaker.ui.main.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.domain.main_menu_navigation.Navigation
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import com.example.playlistmaker.util.Creator

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory(Creator.getMainNavigationInteractor(this))
        )[MainViewModel::class.java]

        viewModel.menuEvent.observe(this){ event ->
            when (event){
                Navigation.Library -> viewModel.openLibrary()
                Navigation.Search -> viewModel.openSearch()
                Navigation.Settings -> viewModel.openSettings()
            }
        }

        binding.searchButton.setOnClickListener {
           viewModel.clickOnSearch()
        }

        binding.libraryButton.setOnClickListener {
            viewModel.clickOnLibrary()
        }

        binding.settingsButton.setOnClickListener {
            viewModel.clickOnSettings()
        }
    }
}
