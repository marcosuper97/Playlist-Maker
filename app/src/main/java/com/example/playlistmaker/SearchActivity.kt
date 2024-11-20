package com.example.playlistmaker
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var searchQuery: String = STR_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchTwo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val trackMeeting: ArrayList<Track> = getMockTracks(this)
        val recViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)
            recViewSearch.adapter = TrackSearchAdapter(trackMeeting)
            recViewSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val searchInput = findViewById<EditText>(R.id.search_hint)
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
            searchInput.setText(searchQuery)
        }
        val clearButton = findViewById<LinearLayout>(R.id.clearButton)
        val searchBack = findViewById<Button>(R.id.search_back)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchQuery = s.toString()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        searchInput.addTextChangedListener(simpleTextWatcher)
        searchInput.requestFocus()
        searchBack.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            searchInput.setText(STR_DEF)
            hideKeyboard(this, clearButton)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY,searchQuery)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
        View.GONE
        } else {
        View.VISIBLE
        }
    }
    private fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    companion object {
        const val KEY_SEARCH_QUERY: String = "SEARCH_QUERY"
        const val STR_DEF: String = ""
    }
}

