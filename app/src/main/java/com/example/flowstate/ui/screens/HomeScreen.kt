package com.example.flowstate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.data.Track
import com.example.flowstate.ui.components.GlassBox
import com.example.flowstate.ui.components.GenreCard
import com.example.flowstate.ui.components.MixCard
import com.example.flowstate.ui.components.TrackCard

@Composable
fun HomeScreen(
    onTrackClick: (Track) -> Unit
) {
    val tracks = remember { MusicRepository.getDummyTracks() }
    val mixes = remember { MusicRepository.getDailyMixes() }
    val genres = remember { MusicRepository.getGenres() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("Relax") }
    
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 180.dp)
        ) {
            item {
                HomeHeader(searchQuery) { searchQuery = it }
            }

            // Mood Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(listOf("Relax", "Workout", "Focus", "Party", "Sleep")) { mood ->
                        MoodChip(
                            mood = mood,
                            isSelected = selectedMood == mood,
                            onClick = { selectedMood = mood }
                        )
                    }
                }
            }

            // Daily Mixes
            item {
                SectionTitle("Your Daily Mixes")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mixes) { mix ->
                        MixCard(mix = mix, onClick = { /* Navigate to Mix */ })
                    }
                }
            }

            // Explore by Genre
            item {
                SectionTitle("Explore by Genre")
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    genres.chunked(2).forEach { rowGenres ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowGenres.forEach { genre ->
                                Box(modifier = Modifier.weight(1f)) {
                                    GenreCard(genre = genre, onClick = { /* Navigate to Genre */ })
                                }
                            }
                        }
                    }
                }
            }

            item {
                SectionTitle("Recently Played")
            }

            items(tracks.take(5)) { track ->
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    TrackCard(track = track, onClick = { onTrackClick(track) })
                }
            }
        }
    }
}

@Composable
fun HomeHeader(query: String, onQueryChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        GlassBox(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search tracks, artists...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun MoodChip(mood: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(mood) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    )
}
