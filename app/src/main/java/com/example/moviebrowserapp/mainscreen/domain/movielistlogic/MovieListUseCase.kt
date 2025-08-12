package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieBody
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListPaginationResponse
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import javax.inject.Inject

class MovieListUseCase @Inject constructor(val movieListRepository: MovieListRepository ){

    suspend fun callMovieList(page :Int) : MovieListPaginationResponse {

         val modalList = ArrayList<MovieListUi>()
        val response = movieListRepository.getMovieList(page)
        response.results.map { modalList.add(it.mapIt()) }
        return MovieListPaginationResponse(
            page = page,
            movieList = modalList
        )

    }


    fun MovieBody.mapIt(): MovieListUi {
        return MovieListUi(
            id = id,
            poster_path = poster_path,
            title = title
        )
    }
}