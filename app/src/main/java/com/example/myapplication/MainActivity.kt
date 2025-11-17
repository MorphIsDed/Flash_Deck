package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.AddCardScreen
import com.example.myapplication.ui.DeckListScreen
import com.example.myapplication.ui.ReviewScreen
import com.example.myapplication.ui.theme.MyApplicationTheme // CHECK THIS: might be FlashDeckTheme
import com.example.myapplication.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // If "MyApplicationTheme" is red, check your ui/theme/Theme.kt file
            // and change this to the name of the function inside it.
            MyApplicationTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = viewModel()

                // Initialize Text-To-Speech
                viewModel.initTTS()

                NavHost(navController = navController, startDestination = "home") {

                    // Screen 1: Home (Deck List)
                    composable("home") {
                        DeckListScreen(navController, viewModel)
                    }

                    // Screen 2: Add Card
                    composable("add_card/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
                        AddCardScreen(navController, viewModel, deckId)
                    }

                    // Screen 3: Review Mode
                    composable("review/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
                        ReviewScreen(viewModel, deckId)
                    }
                }
            }
        }
    }
}