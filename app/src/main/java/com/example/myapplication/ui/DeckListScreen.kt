package com.example.flashdeck.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flashdeck.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(navController: NavController, viewModel: MainViewModel) {
    val allDecks by viewModel.allDecks.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var newDeckName by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("FlashDeck") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Deck")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allDecks) { deck ->
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth().clickable {
                        // Navigate to Review or Add Card? Let's go to Review first
                        navController.navigate("review/${deck.id}")
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = deck.name, style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { navController.navigate("add_card/${deck.id}") }) {
                            Text("Add Cards")
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("New Deck") },
                text = {
                    OutlinedTextField(
                        value = newDeckName,
                        onValueChange = { newDeckName = it },
                        label = { Text("Deck Name") }
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