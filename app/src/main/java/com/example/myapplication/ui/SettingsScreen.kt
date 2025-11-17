package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val currentTheme by settingsViewModel.currentTheme.collectAsState(initial = "tokyo_night")
    val darkMode by settingsViewModel.darkMode.collectAsState(initial = null)
    val fontSize by settingsViewModel.fontSize.collectAsState(initial = 0)
    val highContrast by settingsViewModel.highContrast.collectAsState(initial = false)
    val reduceAnimations by settingsViewModel.reduceAnimations.collectAsState(initial = false)
    val largeText by settingsViewModel.largeText.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Theme Selection
            SettingsSection(title = "Theme") {
                settingsViewModel.themes.forEach { (displayName, themeValue) ->
                    ThemeOption(
                        name = displayName,
                        selected = currentTheme == themeValue,
                        onClick = { settingsViewModel.setTheme(themeValue) }
                    )
                }
            }

            // Dark Mode
            SettingsSection(title = "Appearance") {
                DarkModeOption(
                    darkMode = darkMode,
                    onSystemDefault = { settingsViewModel.setDarkMode(null) },
                    onDarkMode = { settingsViewModel.setDarkMode(true) },
                    onLightMode = { settingsViewModel.setDarkMode(false) }
                )
            }

            // Font Size
            SettingsSection(title = "Font Size") {
                settingsViewModel.fontSizes.forEach { (displayName, sizeValue) ->
                    FontSizeOption(
                        name = displayName,
                        selected = fontSize == sizeValue,
                        onClick = { settingsViewModel.setFontSize(sizeValue) }
                    )
                }
            }

            // Accessibility Options
            SettingsSection(title = "Accessibility") {
                AccessibilityOption(
                    title = "High Contrast",
                    description = "Increase contrast for better visibility",
                    enabled = highContrast,
                    onToggle = { settingsViewModel.setHighContrast(it) }
                )
                
                AccessibilityOption(
                    title = "Large Text",
                    description = "Increase text size throughout the app",
                    enabled = largeText,
                    onToggle = { settingsViewModel.setLargeText(it) }
                )
                
                AccessibilityOption(
                    title = "Reduce Animations",
                    description = "Minimize animations for better performance",
                    enabled = reduceAnimations,
                    onToggle = { settingsViewModel.setReduceAnimations(it) }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
private fun ThemeOption(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DarkModeOption(
    darkMode: Boolean?,
    onSystemDefault: () -> Unit,
    onDarkMode: () -> Unit,
    onLightMode: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = darkMode == null,
                    onClick = onSystemDefault
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = darkMode == null,
                onClick = onSystemDefault
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "System Default",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Follow system theme",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = darkMode == true,
                    onClick = onDarkMode
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = darkMode == true,
                onClick = onDarkMode
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = darkMode == false,
                    onClick = onLightMode
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = darkMode == false,
                onClick = onLightMode
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Light Mode",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun FontSizeOption(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AccessibilityOption(
    title: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle
        )
    }
}

