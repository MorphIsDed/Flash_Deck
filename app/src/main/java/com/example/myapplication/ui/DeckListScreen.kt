package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flashdeck.R
import com.example.myapplication.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(navController: NavController, viewModel: MainViewModel) {
    val allDecks by viewModel.allDecks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newDeckName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredDecks = remember(allDecks, searchQuery) {
        if (searchQuery.isBlank()) {
            allDecks
        } else {
            allDecks.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.my_library),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.tap_to_study),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.profile),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("chat") }) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = stringResource(R.string.ai_study_buddy),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.add_new_deck))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.new_deck))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(padding.calculateTopPadding()))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text(stringResource(R.string.search_decks)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                val deckCount = filteredDecks.size
                val deckLabel = if (searchQuery.isBlank()) {
                    pluralStringResource(R.plurals.deck_count, allDecks.size, allDecks.size)
                } else {
                    pluralStringResource(R.plurals.found_decks, deckCount, deckCount)
                }

                Text(
                    text = deckLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (filteredDecks.isEmpty() && searchQuery.isNotBlank()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_results),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    }

                    items(filteredDecks) { deck ->
                        val scoreText = if (deck.score < 0) "—" else "${deck.score}%"
                        DeckItem(
                            name = deck.name,
                            score = scoreText,
                            onReviewClick = { navController.navigate("review/${deck.id}") },
                            onAddCardClick = { navController.navigate("add_card/${deck.id}") }
                        )
                    }
                }
            }

            FloatingChatButton(
                onClick = { navController.navigate("chat") },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.create_new_deck)) },
                text = {
                    OutlinedTextField(
                        value = newDeckName,
                        onValueChange = { newDeckName = it },
                        label = { Text(stringResource(R.string.deck_name)) },
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
                    }) { Text(stringResource(R.string.create)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) }
                }
            )
        }
    }
}

@Composable
fun DeckItem(name: String, score: String, onReviewClick: () -> Unit, onAddCardClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onReviewClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
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
                    text = stringResource(R.string.deck_score, score),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            IconButton(
                onClick = onAddCardClick,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_cards),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}