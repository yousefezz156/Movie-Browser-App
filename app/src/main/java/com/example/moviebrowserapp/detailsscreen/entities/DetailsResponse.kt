package com.example.moviebrowserapp.detailsscreen.entities

data class DetailsResponse(
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String
)
