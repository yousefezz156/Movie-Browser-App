package com.example.moviebrowserapp.mainscreen.domain.searchquerylogic

import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.SearchQueryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchQueryApiServices {
    @GET("search/movie")
    suspend fun getSearchQuery(@Query("query") query: String, @Query("page") page: Int): SearchQueryResponse

}