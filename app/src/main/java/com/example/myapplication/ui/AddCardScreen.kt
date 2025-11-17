package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    navController: NavController,
    viewModel: MainViewModel,
    deckId: Int
) {
    val isLoading by viewModel.isLoading.collectAsState()
    var frontText by rememberSaveable { mutableStateOf("") }
    var backText by rememberSaveable { mutableStateOf("") }
    var lookupWord by rememberSaveable { mutableStateOf("") }
    var dictionaryResult by remember { mutableStateOf<String?>(null) }
    var textToTranslate by rememberSaveable { mutableStateOf("") }
    var translationResult by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showMessage(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    fun handleAddCard() {
        if (frontText.isBlank() || backText.isBlank()) {
            showMessage("Please fill in both sides")
            return
        }
        viewModel.addCard(deckId, frontText.trim(), backText.trim())
        showMessage("Card saved")
        frontText = ""
        backText = ""
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Cards", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { handleAddCard() },
                icon = { Icon(Icons.Default.Save, contentDescription = "Save Card") },
                text = { Text("Save Card") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Card Content", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = frontText,
                            onValueChange = { frontText = it },
                            label = { Text("Front Text") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = backText,
                            onValueChange = { backText = it },
                            label = { Text("Back Text") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextButton(onClick = { viewModel.speak(backText) }, enabled = backText.isNotBlank()) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null)
                            Spacer(Modifier.size(4.dp))
                            Text("Preview pronunciation")
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Smart Dictionary", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = lookupWord,
                            onValueChange = { lookupWord = it },
                            label = { Text("Lookup Word") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (lookupWord.isBlank()) {
                                    showMessage("Enter a word first")
                                } else {
                                    viewModel.fetchDefinition(lookupWord.trim()) { result ->
                                        scope.launch { dictionaryResult = result }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Fetch Definition")
                        }
                        dictionaryResult?.let { result ->
                            Divider()
                            Text(
                                text = result,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("AI Translation", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = textToTranslate,
                            onValueChange = { textToTranslate = it },
                            label = { Text("Text to translate") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                if (textToTranslate.isBlank()) {
                                    showMessage("Enter some text to translate")
                                } else {
                                    viewModel.translateText(textToTranslate.trim()) { result ->
                                        scope.launch { translationResult = result }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Translate")
                        }
                        translationResult?.let { translated ->
                            Divider()
                            Text(
                                text = translated,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                            RowWithActions(
                                onUse = {
                                    translationResult?.let { backText = it }
                                    showMessage("Filled back text with translation")
                                },
                                onCopy = {
                                    translationResult?.let { backText = it }
                                },
                                onSpeak = { viewModel.speak(translated) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(72.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun RowWithActions(
    onUse: () -> Unit,
    onCopy: () -> Unit,
    onSpeak: () -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssistChip(
            onClick = onUse,
            label = { Text("Use as answer") },
            colors = AssistChipDefaults.assistChipColors()
        )
        AssistChip(
            onClick = onCopy,
            label = { Text("Copy") },
            leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) }
        )
        AssistChip(
            onClick = onSpeak,
            label = { Text("Listen") },
            leadingIcon = { Icon(Icons.Default.VolumeUp, contentDescription = null) }
        )
    }
}