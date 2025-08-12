package com.example.moviebrowserapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviebrowserapp.approutes.appNav
import com.example.moviebrowserapp.mainscreen.presentation.MainScreen
import com.example.moviebrowserapp.ui.theme.MovieBrowserAppTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieBrowserAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    appNav()
                }
            }
        }
    }
}

