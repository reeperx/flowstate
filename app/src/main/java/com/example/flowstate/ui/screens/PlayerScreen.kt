package com.example.flowstate.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowstate.logic.LocalPlaybackManager
import com.example.flowstate.ui.components.AnalysisPanel
import com.example.flowstate.ui.components.GlassVisualizer
import com.example.flowstate.ui.components.PlaybackControls
import com.example.flowstate.ui.theme.LocalFlowStateColors
import kotlin.math.abs

import androidx.compose.ui.tooling.preview.Preview
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.ui.theme.AppTheme
import com.example.flowstate.ui.theme.FlowStateTheme

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    onHaptic: () -> Unit
) {
    val playbackManager = LocalPlaybackManager.current
    val track = playbackManager.currentTrack ?: return
    var showAnalysis by remember { mutableStateOf(false) }
    var totalDragY by remember { mutableFloatStateOf(0f) }
    var totalDragX by remember { mutableFloatStateOf(0f) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        totalDragX = 0f
                        totalDragY = 0f
                    },
                    onDragEnd = {
                        if (abs(totalDragX) > abs(totalDragY)) {
                            if (totalDragX > 100) {
                                playbackManager.previousTrack()
                                onHaptic()
                            } else if (totalDragX < -100) {
                                playbackManager.nextTrack()
                                onHaptic()
                            }
                        } else {
                            if (totalDragY < -100) showAnalysis = true
                            if (totalDragY > 100) showAnalysis = false
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        totalDragX += dragAmount.x
                        totalDragY += dragAmount.y
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Back", modifier = Modifier.size(32.dp))
                }
                Text(
                    text = "NOW PLAYING",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.size(32.dp)) // Spacer
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            GlassVisualizer(bpm = track.bpm, isPlaying = playbackManager.isPlaying)
            
            Spacer(modifier = Modifier.weight(0.5f))
            
            Text(
                text = track.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            PlaybackControls(
                isPlaying = playbackManager.isPlaying,
                onTogglePlay = { 
                    playbackManager.togglePlayback()
                    onHaptic()
                },
                onNext = { 
                    playbackManager.nextTrack()
                    onHaptic()
                },
                onPrevious = { 
                    playbackManager.previousTrack()
                    onHaptic()
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Swipe up for analysis",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
        
        // Scrim background when analysis is open
        if (showAnalysis) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .pointerInput(Unit) {
                        detectDragGestures { _, _ -> /* Block gestures beneath */ }
                    }
                    .clickable { showAnalysis = false }
            )
        }

        AnimatedVisibility(
            visible = showAnalysis,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AnalysisPanel(track = track)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewStudio() {
    val playbackManager = remember { com.example.flowstate.logic.PlaybackManager() }
    val track = com.example.flowstate.data.MusicRepository.getDummyTracks().first()
    playbackManager.playTrack(track)
    
    CompositionLocalProvider(LocalPlaybackManager provides playbackManager) {
        FlowStateTheme(appTheme = AppTheme.Studio) {
            PlayerScreen(onBack = {}, onHaptic = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreviewClub() {
    val playbackManager = remember { com.example.flowstate.logic.PlaybackManager() }
    val track = com.example.flowstate.data.MusicRepository.getDummyTracks().first()
    playbackManager.playTrack(track)
    
    CompositionLocalProvider(LocalPlaybackManager provides playbackManager) {
        FlowStateTheme(appTheme = AppTheme.Club) {
            PlayerScreen(onBack = {}, onHaptic = {})
        }
    }
}
