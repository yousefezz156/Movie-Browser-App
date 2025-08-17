package com.example.moviebrowserapp.mainscreen.entity.movielistentites

import com.example.moviebrowserapp.database.MovieEntity

data class MovieBody(
    val id:Int,
    val poster_path : String,
    val title:String
)

data class MovieResponseBody(
    val list: List<MovieBody>
)