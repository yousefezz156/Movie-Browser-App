package com.example.moviebrowserapp.detailsscreen.domain

import com.example.moviebrowserapp.detailsscreen.entities.DetailsResponse
import com.example.moviebrowserapp.detailsscreen.entities.DetailsUi
import javax.inject.Inject

class DetailsUseCase@Inject constructor(val repo: DetailsRepo )  {

    suspend fun invokeDetails(movieId :Int): DetailsUi {
        val response = repo.getDetails(movieId)
        return response.mapDetailsResponseToUi()

    }

    fun DetailsResponse.mapDetailsResponseToUi(): DetailsUi {
        return DetailsUi(
            overview=overview,
            poster_path=poster_path,
            release_date=release_date,
            title=title
        )
    }

}