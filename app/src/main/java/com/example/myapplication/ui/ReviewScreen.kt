package com.example.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavController, viewModel: MainViewModel, deckId: Int) {
    LaunchedEffect(deckId) {
        viewModel.loadCardsForDeck(deckId)
    }

    val cards by viewModel.currentDeckCards.collectAsState()
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Mode") }, // Hardcoded String
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") // Hardcoded String
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cards.isEmpty()) {
                Text("No cards in this deck yet!") // Hardcoded String
            } else {
                val isFinished = currentIndex >= cards.size

                if (isFinished) {
                    val finalScore = (score * 100) / cards.size

                    Text("Deck Complete!", style = MaterialTheme.typography.headlineSmall) // Hardcoded String
                    Text("Final Score: $finalScore%", style = MaterialTheme.typography.titleLarge) // Hardcoded String
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { currentIndex = 0; isFlipped = false; score = 0 }) {
                        Icon(Icons.Default.Refresh, "Restart")
                        Spacer(Modifier.width(8.dp))
                        Text("Restart") // Hardcoded String
                    }
                } else {
                    Text("Card ${currentIndex + 1} of ${cards.size}", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(16.dp))

                    val currentCard = cards[currentIndex]
                    val textToShow = if (isFlipped) currentCard.backText else currentCard.frontText
                    val label = if (isFlipped) "ANSWER" else "QUESTION"

                    Card(
                        modifier = Modifier.fillMaxWidth().height(300.dp).clickable { if (!isFlipped) isFlipped = true },
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(label, style = MaterialTheme.typography.labelSmall)
                                Spacer(Modifier.height(16.dp))
                                Text(text = textToShow, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    if (isFlipped) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), onClick = {
                                currentIndex++
                                isFlipped = false
                            }) { Text("I Forgot") } // Hardcoded String

                            Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary), onClick = {
                                score++
                                currentIndex++
                                isFlipped = false
                            }) { Text("Got It") } // Hardcoded String
                        }
                    } else {
                        Button(onClick = { viewModel.speak(textToShow) }) {
                            Icon(Icons.Default.PlayArrow, "Speak")
                            Spacer(Modifier.width(8.dp))
                            Text("Listen") // Hardcoded String
                        }
                    }
                }
            }
        }
    }
}