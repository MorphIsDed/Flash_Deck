package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(navController: NavController, viewModel: MainViewModel) {
    val allDecks by viewModel.allDecks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newDeckName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // AESTHETIC UPGRADE: Large Title
            LargeTopAppBar(
                title = {
                    Column {
                        Text("My Library", fontWeight = FontWeight.Bold)
                        Text(
                            "${allDecks.size} Decks",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Existing Settings Button
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, "Add Deck")
                Spacer(Modifier.width(8.dp))
                Text("New Deck")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allDecks) { deck ->
                    DeckItem(
                        name = deck.name,
                        onReviewClick = { navController.navigate("review/${deck.id}") },
                        onAddCardClick = { navController.navigate("add_card/${deck.id}") }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) } // Space for FAB
            }
            
            // Floating Draggable AI Chat Button
            FloatingChatButton(
                onClick = { navController.navigate("chat") },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create New Deck") },
                text = {
                    OutlinedTextField(
                        value = newDeckName,
                        onValueChange = { newDeckName = it },
                        label = { Text("Deck Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (newDeckName.isNotEmpty()) {
                            viewModel.addDeck(newDeckName)
                            newDeckName = ""
                            showDialog = false
                        }
                    }) { Text("Create") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun DeckItem(name: String, onReviewClick: () -> Unit, onAddCardClick: () -> Unit) {
    // AESTHETIC UPGRADE: Gradient Background
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onReviewClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .background(gradientBrush)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tap to study",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            // AESTHETIC UPGRADE: Glassmorphism-style Add Button
            IconButton(
                onClick = onAddCardClick,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Add, "Add Cards", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}