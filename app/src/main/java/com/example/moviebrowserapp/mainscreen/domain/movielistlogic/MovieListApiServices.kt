package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MoiveListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieListApiServices {

    @GET("movie/popular")
    suspend fun MovieList(@Query("page") Page:Int): MoiveListResponse
}