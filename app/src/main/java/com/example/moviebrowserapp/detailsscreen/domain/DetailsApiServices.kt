package com.example.moviebrowserapp.detailsscreen.domain

import com.example.moviebrowserapp.detailsscreen.entities.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsApiServices {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int) : DetailsResponse
}