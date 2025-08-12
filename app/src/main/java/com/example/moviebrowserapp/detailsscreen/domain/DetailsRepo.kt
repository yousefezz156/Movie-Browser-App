package com.example.moviebrowserapp.detailsscreen.domain

import com.example.moviebrowserapp.network.Network
import javax.inject.Inject

class DetailsRepo@Inject constructor(val detailsApiServices: DetailsApiServices ){

    suspend fun getDetails(movieId : Int) = detailsApiServices.getMovieDetails(movieId)
}