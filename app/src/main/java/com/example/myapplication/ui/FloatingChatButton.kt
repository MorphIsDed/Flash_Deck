package com.example.myapplication.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun FloatingChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val buttonSize = with(density) { 64.dp.toPx() }
    
    var offsetX by remember { mutableStateOf(screenWidth - buttonSize - 16f) } // Default to right side
    var offsetY by remember { mutableStateOf(screenHeight - buttonSize - 200f) } // Default to bottom area
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { }, // Make box non-interactive, only button is interactive
        contentAlignment = Alignment.TopStart
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offsetX.roundToInt(),
                        y = offsetY.roundToInt()
                    )
                }
                .size(64.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { },
                        onDragEnd = { },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount.x).coerceIn(0f, screenWidth - buttonSize)
                            offsetY = (offsetY + dragAmount.y).coerceIn(0f, screenHeight - buttonSize)
                        }
                    )
                },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "AI Study Buddy - Drag to move",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

