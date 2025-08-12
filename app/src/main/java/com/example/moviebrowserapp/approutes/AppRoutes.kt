package com.example.moviebrowserapp.approutes

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviebrowserapp.detailsscreen.presentation.DetailsScreen
import com.example.moviebrowserapp.detailsscreen.presentation.DetailsViewModel
import com.example.moviebrowserapp.mainscreen.presentation.MainScreenScaffold
import com.example.moviebrowserapp.mainscreen.presentation.MovieListViewModel

object AppRoutes {

    const val MAINSCREEN = "mainScreen"
    const val DETAILSSCREEN = "detailsScreen"
}


@Composable
fun appNav() {
    val navController = rememberNavController()
    val activity = LocalActivity.current as ComponentActivity

    // Create shared ViewModels at the activity level to persist across navigation
    val movieListViewModel: MovieListViewModel = hiltViewModel()
    val detailsViewModel: DetailsViewModel = hiltViewModel()

    DisposableEffect(Unit) {
        Log.d("NavDebug", "appNav() recomposed")
        onDispose {
            Log.d("NavDebug", "appNav() disposed")
        }
    }
    NavHost(navController = navController, startDestination = AppRoutes.MAINSCREEN) {

        composable(route = AppRoutes.MAINSCREEN) {
            MainScreenScaffold(movieListViewModel = movieListViewModel, navController = navController)
        }
        composable(
            route = "${AppRoutes.DETAILSSCREEN}/{movieId}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            if (movieId != null) {
                DetailsScreen(movieId = movieId, detailsViewModel = detailsViewModel)
            }
        }
    }
}