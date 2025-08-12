package com.example.moviebrowserapp.mainscreen.domain.searchquerylogic

import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.SearchQueryBody
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.SearchQueryResponse
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryPagination
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi
import javax.inject.Inject

class SearchQueryUseCase @Inject constructor(val repo: SearchQueryRepo ) {

    suspend fun callSearchQuery(query: String, page: Int): SearchQueryPagination {

        val modalList = ArrayList<SearchQueryUi>()
        val response = repo.getSearchQuery(query= query, page = page)
        response.results.map { modalList.add(it.mapIt()) }
        return SearchQueryPagination(page = page, results = modalList)
    }

    fun SearchQueryBody.mapIt(): SearchQueryUi {
        return SearchQueryUi(id = id, poster_path = poster_path, title = title)
    }
}