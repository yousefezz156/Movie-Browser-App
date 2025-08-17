package com.example.moviebrowserapp.mainscreen.presentation

sealed class MainScreenIntent {

    object LoadMovieOffline : MainScreenIntent()
    data class SearchQuery(val query: String) : MainScreenIntent()
    object RefreshScreen : MainScreenIntent()
    object CheckNetworkConnection : MainScreenIntent()
    object Retry : MainScreenIntent()
}