package com.example.moviebrowserapp.mainscreen.presentation

sealed class MainScreenIntent {

    object LoadMovie : MainScreenIntent()
    data class SearchQuery(val query: String) : MainScreenIntent()
    object RefreshScreen : MainScreenIntent()
    object Retry : MainScreenIntent()
}