package com.example.ghusers.ui.features

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ghusers.model.NavigationState
import com.example.ghusers.ui.features.details.DetailsScreen
import com.example.ghusers.ui.features.home.ListScreen
import com.example.ghusers.ui.theme.GHUsersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GHUsersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController , startDestination = NavigationState.MAIN.value) {
                        composable(NavigationState.MAIN.value) {
                            ListScreen(onItemSelectd = {
                                navController.navigate(route = NavigationState.details(it).value)
                            })
                        }
                        composable(NavigationState.DETAILS.value) {entry ->
                            entry.arguments?.getString("user")?.let { user ->
                                DetailsScreen(
                                    userLogin = user,
                                    onBack = { navController.popBackStack() },
                                    onShare = {shareUrl(it)},
                                    openUrl = {openUrl(it)}
                                )
                            } ?: LaunchedEffect(Unit){
                                navController.navigate(route = NavigationState.MAIN.value)
                            }

                        }
                    }

                }
            }
        }
    }

    private fun openUrl( url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun shareUrl( url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
    }
}