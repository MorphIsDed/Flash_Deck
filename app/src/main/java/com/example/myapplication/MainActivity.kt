package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.AboutScreen
import com.example.myapplication.ui.AddCardScreen
import com.example.myapplication.ui.ChatScreen
import com.example.myapplication.ui.DeckListScreen
import com.example.myapplication.ui.FeedbackScreen
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.ui.ReviewScreen
import com.example.myapplication.ui.SettingsScreen
import com.example.myapplication.ui.SplashScreen
import com.example.myapplication.ui.StatisticsScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.ProfileViewModel
import com.example.myapplication.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize TTS Engine immediately so it's ready for Review Mode
        viewModel.initTTS()

        setContent {
            val currentTheme by settingsViewModel.currentTheme.collectAsState(initial = "tokyo_night")
            val darkModePreference by settingsViewModel.darkMode.collectAsState(initial = null)
            val fontSize by settingsViewModel.fontSize.collectAsState(initial = 0)
            val highContrast by settingsViewModel.highContrast.collectAsState(initial = false)
            val largeText by settingsViewModel.largeText.collectAsState(initial = false)

            val isDarkTheme = darkModePreference ?: isSystemInDarkTheme()

            MyApplicationTheme(
                darkTheme = isDarkTheme,
                theme = currentTheme,
                fontSize = fontSize,
                highContrast = highContrast,
                largeText = largeText
            ) {
                AppNavigation(
                    navController = rememberNavController(),
                    viewModel = viewModel,
                    settingsViewModel = settingsViewModel,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: androidx.navigation.NavHostController,
    viewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    profileViewModel: ProfileViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        // 1. Splash Screen
        composable("splash") {
            SplashScreen { navController.navigate("home") { popUpTo("splash") { inclusive = true } } }
        }

        // 2. Home (Deck List)
        composable("home") {
            DeckListScreen(navController, viewModel)
        }

        // 3. Add Card Screen
        composable("add_card/{deckId}") { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
            AddCardScreen(navController, viewModel, deckId)
        }

        // 4. Review Mode
        composable("review/{deckId}") { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString("deckId")?.toIntOrNull() ?: 0
            ReviewScreen(navController, viewModel, deckId)
        }

        // 5. Settings Screen
        composable("settings") {
            SettingsScreen(navController, settingsViewModel)
        }

        // 6. AI Chatbot Route (The Study Buddy)
        composable("chat") {
            ChatScreen(navController)
        }

        // 7. About Screen
        composable("about") {
            AboutScreen(navController)
        }

        // 8. Feedback Screen
        composable("feedback") {
            FeedbackScreen(navController)
        }

        // 9. Statistics Screen
        composable("statistics") {
            StatisticsScreen(navController, viewModel)
        }

        // 10. Profile Screen
        composable("profile") {
            ProfileScreen(navController, profileViewModel)
        }
    }
}