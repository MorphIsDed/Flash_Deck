package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.AddCardScreen
import com.example.myapplication.ui.DeckListScreen
import com.example.myapplication.ui.ReviewScreen
import com.example.myapplication.ui.SplashScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen { navController.navigate("home") { popUpTo("splash") { inclusive = true } } }
                    }

                    composable("home") {
                        DeckListScreen(navController, viewModel)
                    }

                    composable("add_card/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
                        AddCardScreen(navController, viewModel, deckId)
                    }

                    composable("review/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
                        ReviewScreen(navController, viewModel, deckId)
                    }
                }
            }
        }
    }
}