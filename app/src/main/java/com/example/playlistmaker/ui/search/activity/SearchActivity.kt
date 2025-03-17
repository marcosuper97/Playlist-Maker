package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.State
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.util.GsonClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchActivity : AppCompatActivity() {
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private var searchQuery: String = STR_DEF
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel{ parametersOf(this@SearchActivity) }
    private val playerIntent: Intent by lazy {
        Intent(this, PlayerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.screenState.observe(this) { state ->
            when (state) {
                State.EmptyScreen -> emptyUi()
                State.Loading -> loadingUi()
                State.NetworkError -> networkErrorUi()
                State.NothingFound -> tracksNotFoundUi()
                is State.ShowHistoryContent -> historyUi(state.tracks)
                is State.ShowSearchContent -> contentUi(state.tracks)
            }
        }

        val onTrackClickListener: (Track) -> Unit = { track ->
            viewModel.onTrackClicked(track)
            playerIntent.putExtra("track", GsonClient.objectToJson(track))
            this.startActivity(playerIntent)
        }

        searchAdapter = SearchAdapter(onTrackClickListener)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = searchAdapter

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                if (s != null) {
                    searchQuery = s.toString()
                    searchRequest()
                }else {
                    handler.removeCallbacks(searchRunnable)
                }
            }
        }

        binding.searchHint.addTextChangedListener(simpleTextWatcher)

        binding.clearSearchHistory.setOnClickListener() {
            viewModel.onClickSearchClear()
        }

        binding.searchUpdate.setOnClickListener {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        binding.searchBack.setNavigationOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            hideKeyboard(this, binding.clearButton)
            binding.searchHint.setText(STR_DEF)
            viewModel.clickOnClearButton()
        }

        binding.searchUpdate.setOnClickListener(){
            searchRequest()
        }

        binding.searchHint.requestFocus()
    }

    private fun emptyUi() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.youSearchedIt.visibility = View.GONE
        binding.clearSearchHistory.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
    }

    private fun loadingUi() {
        emptyUi()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun contentUi(content: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.youSearchedIt.visibility = View.GONE
        searchAdapter.tracks = content
        searchAdapter.notifyDataSetChanged()
        binding.clearSearchHistory.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
        binding.youSearchedIt.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun historyUi(history: List<Track>) {
        binding.progressBar.visibility = View.GONE
        searchAdapter.tracks = history
        searchAdapter.notifyDataSetChanged()
        binding.youSearchedIt.visibility = View.VISIBLE
        binding.clearSearchHistory.visibility = View.VISIBLE
        binding.searchPlaceholder.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.clearButton.visibility = View.GONE
    }

    private fun networkErrorUi() {
        binding.progressBar.visibility = View.GONE
        binding.youSearchedIt.visibility = View.GONE
        binding.clearSearchHistory.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchUpdate.visibility = View.VISIBLE
        binding.errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.network_error))
        binding.errorStatus.setText(R.string.network_error)
    }

    private fun tracksNotFoundUi() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchUpdate.visibility = View.GONE
        binding.errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.tracks_not_found))
        binding.errorStatus.setText(R.string.tracks_not_found)
    }

    private fun searchRequest() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private val searchRunnable = Runnable {
        if (searchQuery.isNotEmpty()) {
            viewModel.toSearchRequest(searchQuery)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val STR_DEF: String = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}