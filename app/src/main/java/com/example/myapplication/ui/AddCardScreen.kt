package com.example.flashdeck.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flashdeck.R
import com.example.flashdeck.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navController: NavController, viewModel: MainViewModel, deckId: Int) {
    var frontText by remember { mutableStateOf("") }
    var backText by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.add_new_card)) }) }
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
                label = { Text(stringResource(R.string.front_word_question)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.fetchDefinition(frontText) { res -> backText = res } },
                        enabled = frontText.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Search, stringResource(R.string.auto_define))
                    }
                }
            )
            Text(stringResource(R.string.tap_to_auto_define), style = MaterialTheme.typography.bodySmall)

            // --- BACK TEXT (With Translate Button) ---
            OutlinedTextField(
                value = backText,
                onValueChange = { backText = it },
                label = { Text(stringResource(R.string.back_definition_answer)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.translateText(frontText) { res -> backText = res } },
                        enabled = frontText.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Language, stringResource(R.string.translate))
                    }
                }
            )
            Text(stringResource(R.string.tap_to_translate), style = MaterialTheme.typography.bodySmall)

            if (isLoading) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = {
                    if (frontText.isNotEmpty() && backText.isNotEmpty()) {
                        viewModel.addCard(deckId, frontText, backText)
                        navController.popBackStack()
                    }
                },
                enabled = frontText.isNotEmpty() && backText.isNotEmpty()
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.save_card))
            }
        }
    }
}