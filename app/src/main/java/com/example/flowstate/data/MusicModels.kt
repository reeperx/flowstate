package com.example.flowstate.data

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val bpm: Int,
    val key: String,
    val genre: String,
    val coverRes: Int = 0 // Placeholder
)

enum class SequencingMode {
    Ascending, Descending, Valley, Peak, Smart
}

data class Mix(
    val id: String,
    val title: String,
    val description: String,
    val color: androidx.compose.ui.graphics.Color
)

data class Genre(
    val id: String,
    val name: String,
    val gradient: List<androidx.compose.ui.graphics.Color>
)

data class Playlist(
    val id: String,
    val title: String,
    val trackCount: Int,
    val gradient: List<androidx.compose.ui.graphics.Color>
)

data class ActivitySession(
    val id: String,
    val date: String,
    val averageSpm: Int,
    val duration: String,
    val totalSteps: Int
)
