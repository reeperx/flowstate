package com.example.flowstate.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.logic.LocalStepSensorManager
import com.example.flowstate.logic.LocalPlaybackManager
import com.example.flowstate.ui.components.GlassBox
import com.example.flowstate.ui.components.PaceGraph
import com.example.flowstate.ui.components.SessionHistoryCard
import com.example.flowstate.ui.theme.FlowStateTheme
import com.example.flowstate.ui.theme.AppTheme
import com.example.flowstate.logic.StepSensorManager
import com.example.flowstate.logic.PlaybackManager
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ActivityScreen() {
    val stepManager = LocalStepSensorManager.current
    val playbackManager = LocalPlaybackManager.current
    val spm by stepManager.spm.collectAsState()
    val history = remember { MusicRepository.getActivityHistory() }
    
    var isSessionActive by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    val paceData = remember { mutableStateListOf<Int>() }

    val bgColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary
    val bgBrush = remember(bgColor, primaryColor) {
        Brush.verticalGradient(
            colors = listOf(
                bgColor,
                primaryColor.copy(alpha = 0.05f),
                bgColor
            )
        )
    }

    // Session Timer & Data Logging
    LaunchedEffect(isSessionActive) {
        if (isSessionActive) {
            stepManager.startListening()
            elapsedSeconds = 0
            paceData.clear()
            while (isSessionActive) {
                delay(1.seconds)
                elapsedSeconds++
                if (elapsedSeconds % 5 == 0) { // Log pace every 5 seconds
                    paceData.add(spm)
                    if (paceData.size > 20) paceData.removeAt(0)
                }
            }
        } else {
            stepManager.stopListening()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        AnimatedContent(
            targetState = isSessionActive,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "SessionState"
        ) { active ->
            if (active) {
                ActiveSessionView(
                    spm = spm,
                    elapsedSeconds = elapsedSeconds,
                    paceData = paceData,
                    currentTrackBpm = playbackManager.currentTrack?.bpm ?: 0,
                    onStop = { isSessionActive = false }
                )
            } else {
                IdleActivityView(
                    history = history,
                    onStart = { isSessionActive = true }
                )
            }
        }
    }
}

@Composable
fun ActiveSessionView(
    spm: Int,
    elapsedSeconds: Int,
    paceData: List<Int>,
    currentTrackBpm: Int,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "LIVE FLOW",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        // Pulse Ring
        val infiniteTransition = rememberInfiniteTransition()
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = if (spm > 0) (60000 / spm) else 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
        ) {
            GlassBox(modifier = Modifier.fillMaxSize(), cornerRadius = 120.dp) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = spm.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "STEPS / MIN",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Pace Graph
        PaceGraph(
            data = paceData,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "ELAPSED", value = formatTime(elapsedSeconds))
            StatItem(label = "SYNC BPM", value = currentTrackBpm.toString())
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStop,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("End Session", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun IdleActivityView(
    history: List<com.example.flowstate.data.ActivitySession>,
    onStart: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                GlassBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    cornerRadius = 24.dp
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ready to flow?",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Start a session to sync music with your movement.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recent Sessions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            items(history) { session ->
                SessionHistoryCard(session = session)
            }
            
            item { Spacer(modifier = Modifier.height(340.dp)) }
        }

        ExtendedFloatingActionButton(
            onClick = onStart,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 260.dp), // Increased padding to clear navbar
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(32.dp)
        ) {
            Icon(Icons.Default.DirectionsRun, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Start Session", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}

@Preview(showBackground = true)
@Composable
fun ActivityPreviewStudio() {
    val playbackManager = remember { com.example.flowstate.logic.PlaybackManager() }
    val stepManager = remember { com.example.flowstate.logic.MockStepSensorManager() }
    
    CompositionLocalProvider(
        LocalStepSensorManager provides stepManager,
        LocalPlaybackManager provides playbackManager
    ) {
        FlowStateTheme(appTheme = AppTheme.Studio) {
            ActivityScreen()
        }
    }
}
