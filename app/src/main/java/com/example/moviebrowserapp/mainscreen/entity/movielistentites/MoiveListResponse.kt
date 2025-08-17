package com.example.moviebrowserapp.mainscreen.entity.movielistentites

data class MovieListResponse(
    val page :Int,
    val results:List<MovieBody>,
    val total_page :Int,
    val total_results :Int
)
