package com.example.moviebrowserapp.mainscreen.entity.searchqueryentites

data class SearchQueryResponse(
val page :Int,
val results:ArrayList<SearchQueryBody>,
val total_page :Int,
val total_pages :Int
)
