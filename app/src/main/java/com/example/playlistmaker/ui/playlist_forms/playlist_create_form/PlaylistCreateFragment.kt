package com.example.playlistmaker.ui.playlist_forms.playlist_create_form

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistFormBinding
import com.example.playlistmaker.presentation.playlist_create.PlaylistCreateViewModel
import com.example.playlistmaker.ui.playlist_forms.states.PlaylistCreateState
import com.example.playlistmaker.util.extension.FragmentSnackExtension.showSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class PlaylistCreateFragment : Fragment() {

    private var _binding: FragmentPlaylistFormBinding? = null
    protected val binding get() = _binding!!
    protected var playlistName: String = ""
    protected var playlistTitle: String? = null
    protected var imageUri: Uri? = null
    protected open val viewModel: PlaylistCreateViewModel by viewModel{ parametersOf(requireContext()) }
    private val requester = PermissionRequester.instance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    protected val playlistNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when {
                s.isNullOrEmpty() || s.toString().trim()
                    .isEmpty() -> binding.applyButton.isEnabled = false

                else -> {
                    binding.applyButton.isEnabled = true
                    playlistName = s.toString()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
        stateObserver()
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.poster.setImageURI(uri)
                    imageUri = uri
                }
            }

        val playlistDescriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.isNullOrEmpty() || s.toString().trim().isEmpty() -> playlistTitle = null
                    else -> playlistTitle = s.toString()
                }
            }
        }

        binding.poster.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )

                        is PermissionResult.Denied.NeedsRationale -> showSnackBar(
                            binding.root,
                            getString(R.string.settingsPermissionDialogTitle)
                        )

                        is PermissionResult.Denied.DeniedPermanently -> showSettingsDialog()
                        PermissionResult.Cancelled -> showSnackBar(
                            binding.root,
                            getString(R.string.settingsPermissionDialogTitle)
                        )
                    }
                }
            }
        }
        binding.playlistNameEditText.addTextChangedListener(playlistNameTextWatcher)
        binding.playlistDescriptionEditText.addTextChangedListener(playlistDescriptionTextWatcher)
        binding.back.setOnClickListener() {
            exitDialog()
        }

        binding.applyButton.setOnClickListener {
            applyButton()
        }
    }

    protected open fun exitDialog() {
        if (playlistName.isNotEmpty() || playlistTitle != null || imageUri != null) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlistExitDialogTitle))
                .setMessage(getString(R.string.playlistExitDialogDescription))
                .setPositiveButton(getString(R.string.complete)) { _, _ ->
                    parentFragment?.findNavController()?.navigateUp()
                }
                .setNegativeButton(getString(R.string.cancellation), null)
                .show()
        } else parentFragment?.findNavController()?.navigateUp()
    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.settingsPermissionDialogTitle))
            .setMessage(getString(R.string.settingsPermissionDialogDescription))
            .setPositiveButton(getString(R.string.settings)) { _, _ -> openAppSettings() }
            .setNegativeButton(getString(R.string.cancellation), null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun applyButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            when (imageUri) {
                null -> {
                    viewModel.playlistApply(playlistName, playlistTitle, null)
                    delay(TIME_DELAY_SHORT)
                    parentFragment?.findNavController()?.navigateUp()
                }

                else -> {
                    viewModel.playlistApply(
                        playlistName,
                        playlistTitle,
                        imageUri.toString()
                    )
                    delay(TIME_DELAY_LENGTH)
                    parentFragment?.findNavController()?.navigateUp()
                }
            }
        }
    }

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitDialog()
        }
    }

    protected open fun stateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlistFormState.collect() { state ->
                when (state) {
                    is PlaylistCreateState.Default -> Unit
                    is PlaylistCreateState.Denied -> showSnackBar(binding.root, state.errorMessage)
                    is PlaylistCreateState.Success -> {
                        showSnackBar(binding.root, state.successMessage)
                    }
                    else -> Unit
                }
            }
        }
    }

    companion object {
        private const val TIME_DELAY_SHORT = 300L
        private const val TIME_DELAY_LENGTH = 600L
    }
}