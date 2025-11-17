package com.example.myapplication.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Session", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cards.isEmpty()) {
                EmptyStateMessage()
            } else {
                val isFinished = currentIndex >= cards.size

                if (isFinished) {
                    FinishedState(
                        onRestart = {
                            currentIndex = 0
                            isFlipped = false
                        },
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1) / cards.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(bottom = 24.dp),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )

                    Text(
                        text = "Card ${currentIndex + 1} of ${cards.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3D Flip Card Component
                    val currentCard = cards[currentIndex]

                    FlipCard(
                        isFlipped = isFlipped,
                        frontText = currentCard.frontText,
                        backText = currentCard.backText,
                        onFlip = { isFlipped = !isFlipped }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Controls
                    if (!isFlipped) {
                        // Front Controls
                        Button(
                            onClick = { isFlipped = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Show Answer")
                        }
                    } else {
                        // Back Controls (SRS Logic)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // TTS Button
                            FilledTonalButton(
                                onClick = { viewModel.speak(currentCard.backText) }
                            ) {
                                Icon(Icons.Default.PlayArrow, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Pronounce")
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // "I Forgot" Button
                                Button(
                                    onClick = {
                                        viewModel.recordCardReview(currentCard.id, isCorrect = false)
                                        isFlipped = false
                                        currentIndex++
                                    },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.Close, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Forgot")
                                }

                                // "I Know It" Button
                                Button(
                                    onClick = {
                                        viewModel.recordCardReview(currentCard.id, isCorrect = true)
                                        isFlipped = false
                                        currentIndex++
                                    },
                                    modifier = Modifier.weight(1f).height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Hardcoded Success Green
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.Check, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Got it")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlipCard(
    isFlipped: Boolean,
    frontText: String,
    backText: String,
    onFlip: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "flip_anim"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { onFlip() }
    ) {
        if (rotation <= 90f) {
            // Front Face
            CardFace(
                text = frontText,
                label = "QUESTION",
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        } else {
            // Back Face (Rotate content 180 so it's readable)
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                CardFace(
                    text = backText,
                    label = "ANSWER",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun CardFace(text: String, label: String, backgroundColor: Color, textColor: Color) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor.copy(alpha = 0.6f),
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No cards found.\nAdd some to start learning!", textAlign = TextAlign.Center)
    }
}

@Composable
fun FinishedState(onRestart: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text("Session Complete!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("You reviewed all cards.", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

        Spacer(Modifier.height(32.dp))

        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Icon(Icons.Default.Refresh, null)
            Spacer(Modifier.width(8.dp))
            Text("Review Again")
        }
        TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back to Decks")
        }
    }
}