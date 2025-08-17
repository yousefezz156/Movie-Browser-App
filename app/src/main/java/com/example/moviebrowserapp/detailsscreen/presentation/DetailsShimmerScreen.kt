package com.example.moviebrowserapp.detailsscreen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun DetailsShimmerScreen(isLoading: Boolean, modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(28.dp)
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                        shape = RoundedCornerShape(6.dp)
                    )
            )

            Card(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
            ){}

        }
    }
}