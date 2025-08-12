package com.example.moviebrowserapp.mainscreen.entity.movielistentites

data class MoiveListResponse(
    val page :Int,
    val results:Array<MovieBody>,
    val total_page :Int,
    val total_pages :Int
)
