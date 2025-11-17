package com.example.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(viewModel: MainViewModel, deckId: Int) {
    // Load cards when this screen opens
    LaunchedEffect(deckId) {
        viewModel.loadCardsForDeck(deckId)
    }

    val cards by viewModel.currentDeckCards.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) } // false = Front, true = Back

    Scaffold(
        topBar = { TopAppBar(title = { Text("Review Mode") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cards.isEmpty()) {
                Text("No cards in this deck yet!")
            } else {
                // Progress Counter
                Text("Card ${currentIndex + 1} / ${cards.size}", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(16.dp))

                // The Flashcard
                val currentCard = cards.getOrNull(currentIndex)

                if (currentCard != null) {
                    // FIXED: Changed .back/.front to .backText/.frontText
                    val textToShow = if (isFlipped) currentCard.backText else currentCard.frontText
                    val label = if (isFlipped) "ANSWER" else "QUESTION"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clickable { isFlipped = !isFlipped }, // Flip on tap
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(label, style = MaterialTheme.typography.labelSmall)
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = textToShow,
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Controls
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // TTS Button
                        Button(onClick = { viewModel.speak(textToShow) }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Speak")
                            Spacer(Modifier.width(8.dp))
                            Text("Listen")
                        }

                        // Next Button
                        Button(
                            enabled = currentIndex < cards.size - 1,
                            onClick = {
                                currentIndex++
                                isFlipped = false // Reset flip
                            }
                        ) {
                            Text("Next Card")
                        }
                    }
                }
            }
        }
    }
}