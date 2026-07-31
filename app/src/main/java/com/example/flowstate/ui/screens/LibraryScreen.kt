package com.example.flowstate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.data.SequencingMode
import com.example.flowstate.data.Track
import com.example.flowstate.logic.LocalPlaybackManager
import com.example.flowstate.ui.components.GlassBox
import com.example.flowstate.ui.components.PlaylistCard
import com.example.flowstate.ui.components.TrackCard
import com.example.flowstate.ui.theme.AppTheme
import com.example.flowstate.ui.theme.FlowStateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    val playbackManager = LocalPlaybackManager.current
    var sequencingMode by remember { mutableStateOf(SequencingMode.Ascending) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tracks", "Playlists", "Folders")
    
    val allTracks = remember { MusicRepository.getDummyTracks() }
    val allPlaylists = remember { MusicRepository.getPlaylists() }
    
    val sortedTracks = remember(sequencingMode) {
        when (sequencingMode) {
            SequencingMode.Ascending -> allTracks.sortedBy { it.bpm }
            SequencingMode.Descending -> allTracks.sortedByDescending { it.bpm }
            SequencingMode.Valley -> {
                val sorted = allTracks.sortedBy { it.bpm }
                val half = sorted.size / 2
                sorted.takeLast(half).reversed() + sorted.take(sorted.size - half)
            }
            SequencingMode.Peak -> {
                val sorted = allTracks.sortedBy { it.bpm }
                val half = sorted.size / 2
                sorted.take(half) + sorted.takeLast(sorted.size - half).reversed()
            }
            SequencingMode.Smart -> allTracks.shuffled()
        }
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Library Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTab) {
                0 -> { // Tracks View
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Intelligent Sequencing Chips
                        Box(modifier = Modifier.weight(1f)) {
                            ScrollableTabRow(
                                selectedTabIndex = sequencingMode.ordinal,
                                edgePadding = 0.dp,
                                containerColor = Color.Transparent,
                                divider = {},
                                indicator = {}
                            ) {
                                SequencingMode.entries.forEach { mode ->
                                    val isSelected = sequencingMode == mode
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { sequencingMode = mode },
                                        label = { Text(mode.name) },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                        
                        IconButton(onClick = { /* Generate Smart Mix */ }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Smart Mix", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        items(sortedTracks) { track ->
                            TrackCard(track = track, onClick = { playbackManager.playTrack(track) })
                        }
                        item { Spacer(modifier = Modifier.height(160.dp)) }
                    }
                }
                1 -> { // Playlists View
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(allPlaylists) { playlist ->
                            PlaylistCard(playlist = playlist, onClick = { /* Navigate to Playlist */ })
                        }
                        item { Spacer(modifier = Modifier.height(160.dp)) }
                    }
                }
                2 -> { // Folders View
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Local folders coming soon", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryPreviewStudio() {
    val playbackManager = remember { com.example.flowstate.logic.PlaybackManager() }
    CompositionLocalProvider(LocalPlaybackManager provides playbackManager) {
        FlowStateTheme(appTheme = AppTheme.Studio) {
            LibraryScreen()
        }
    }
}
