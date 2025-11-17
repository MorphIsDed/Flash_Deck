package com.example.myapplication.ui // Matches your folder structure

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh // Changed from Translate to Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navController: NavController, viewModel: MainViewModel, deckId: Int) {
    var frontText by remember { mutableStateOf("") }
    var backText by remember { mutableStateOf("") }
    // Fix: Added explicit type to avoid recursion errors
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    Scaffold(
        // FIXED: Using hardcoded string instead of R.string
        topBar = { TopAppBar(title = { Text("Add New Card") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- FRONT TEXT (With Dictionary Button) ---
            OutlinedTextField(
                value = frontText,
                onValueChange = { frontText = it },
                label = { Text("Front (Word/Question)") }, // Hardcoded string
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.fetchDefinition(frontText) { res -> backText = res } },
                        enabled = frontText.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Auto-Define")
                    }
                }
            )
            Text("Tap the magnifying glass to Auto-Define", style = MaterialTheme.typography.bodySmall)

            // --- BACK TEXT (With Translate Button) ---
            OutlinedTextField(
                value = backText,
                onValueChange = { backText = it },
                label = { Text("Back (Definition/Answer)") }, // Hardcoded string
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.translateText(frontText) { res -> backText = res } },
                        enabled = frontText.isNotEmpty()
                    ) {
                        // FIXED: Changed 'Translate' to 'Refresh' (Standard Icon)
                        Icon(Icons.Default.Refresh, contentDescription = "Translate")
                    }
                }
            )
            Text("Tap icon to Translate", style = MaterialTheme.typography.bodySmall)

            if (isLoading) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = {
                    viewModel.addCard(deckId, frontText, backText)
                    navController.popBackStack()
                },
                enabled = frontText.isNotEmpty() && backText.isNotEmpty()
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Save Card") // Hardcoded string
            }
        }
    }
}