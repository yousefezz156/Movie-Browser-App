package com.example.moviebrowserapp.network.httpsurlconnection

import android.util.Log
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieBody
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieListResponse
import com.example.moviebrowserapp.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class MovieHttpsUrlConnection {

    suspend fun fetchMovies(page :Int): String = withContext(Dispatchers.IO) {
       val urlString = "https://api.themoviedb.org/3/movie/popular?page=$page"
        var connection:HttpsURLConnection? = null

        return@withContext try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpsURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer ${Network.token}")
            connection.setRequestProperty("accept" , "application/json")

            val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
            return@withContext response
        } catch (e: Exception) {
           throw e
        } finally {
            connection?.disconnect()
        }
    }
//    override suspend fun getMovie(page: Int): MovieListResponse = withContext(Dispatchers.IO) {
//        val urlString = "https://api.themoviedb.org/3/movie/popular?page=$page"
//        Log.d("PagingDebug", "Preparing request for URL: $urlString")
//
//        var connection: HttpsURLConnection? = null
//
//        try {
//            val url = URL(urlString)
//            connection = url.openConnection() as HttpsURLConnection
//
//            // Set request method and headers FIRST
//            connection.requestMethod = "GET"
//            connection.setRequestProperty("Authorization", "Bearer ${Network.token}")
//            connection.setRequestProperty("Accept", "application/json")
//
//
//
//
//
//
//
//
//            val responseBody = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
//
//            val json = JSONObject(responseBody)
//            val results = json.getJSONArray("results")
//            val movieList = mutableListOf<MovieBody>()
//
//            Log.d("PagingDebug", "Parsing ${results.length()} movies...")
//
//            for (i in 0 until results.length()) {
//                val movieJson = results.getJSONObject(i)
//                val movie = MovieBody(
//                    id = movieJson.getInt("id"),
//                    poster_path = movieJson.getString("poster_path"),
//                    title = movieJson.getString("title")
//                )
//                movieList.add(movie)
//            }
//
//            Log.d("PagingDebug", "Parsed ${movieList.size} movies successfully")
//
//            return@withContext MovieListResponse(
//                page = json.getInt("page"),
//                results = movieList,
//                total_page = json.getInt("total_pages"),
//                total_results = json.getInt("total_results")
//            )
//
//        } catch (e: Exception) {
//            Log.e("PagingDebug", "Error during request: ${e.message}", e)
//            throw e
//        } finally {
//            Log.d("PagingDebug", "Closing connection")
//            connection?.disconnect()
//        }
//    }
}