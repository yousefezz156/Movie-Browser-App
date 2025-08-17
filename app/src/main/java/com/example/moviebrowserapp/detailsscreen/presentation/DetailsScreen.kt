package com.example.moviebrowserapp.detailsscreen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun DetailsScreen(
    movieId: Int,
    detailsViewModel: DetailsViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(movieId) {
        detailsViewModel.getDetails(movieId)
    }

    val detailsState by detailsViewModel.details.collectAsState()
    val errorState by detailsViewModel.error.collectAsState()
    val isLoading by detailsViewModel.isLoading.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            isLoading -> {
                DetailsShimmerScreen(isLoading = isLoading)
            }

            detailsState != null -> {


                val movie = detailsState!!
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(520.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(520.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f)
                                        )
                                    )
                                )
                        )

                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 32.dp, start = 65.dp, end = 24.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                            Text(
                                text = "Release Date: ${movie.release_date}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Yellow)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Divider(color = Color.Gray, thickness = 0.5.dp)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}


@Preview
@Composable
private fun DetailsScreenPrev() {
}