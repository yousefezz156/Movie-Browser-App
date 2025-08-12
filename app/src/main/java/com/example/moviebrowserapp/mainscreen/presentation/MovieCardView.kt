package com.example.moviebrowserapp.mainscreen.presentation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebrowserapp.approutes.AppRoutes

import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi

@Composable
fun MovieCardView(navController: NavController, movieListUi: MovieListUi, modifier: Modifier = Modifier) {

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        .border(border = BorderStroke(2.dp, color = Color.Yellow))) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .clickable { navController.navigate("${AppRoutes.DETAILSSCREEN}/${movieListUi.id}") }
        )
        {
            val imageUrl = "https://image.tmdb.org/t/p/w500${movieListUi.poster_path}"
            Log.d("image", imageUrl)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Movie poster for ${movieListUi.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(height = 150.dp, width = 150.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = movieListUi.title, color = Color.White, fontSize = 12.sp)

        }
    }
}

@Composable
fun SearchQueryCardView( searchQueryUi: SearchQueryUi,navController: NavController ,modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        .border(border = BorderStroke(2.dp, color = Color.Yellow))) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .clickable { navController.navigate("${AppRoutes.DETAILSSCREEN}/${searchQueryUi.id}") }
        )
        {
            val imageUrl = "https://image.tmdb.org/t/p/w500${searchQueryUi.poster_path}"
            Log.d("image", imageUrl)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Movie poster for ${searchQueryUi.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(height = 150.dp, width = 150.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )

            searchQueryUi.poster_path?.let { Log.d("PosterPath", it) }
            Spacer(modifier = Modifier.width(16.dp))

            Text(text = searchQueryUi.title, color = Color.Yellow, fontSize = 12.sp)

        }
    }
}
