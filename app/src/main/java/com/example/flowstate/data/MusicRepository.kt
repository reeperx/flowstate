package com.example.flowstate.data

import androidx.compose.ui.graphics.Color

object MusicRepository {
    fun getDummyTracks(): List<Track> = listOf(
        Track("1", "Neon Dreams", "Synthwave Master", "3:45", 124, "Am", "Synthwave"),
        Track("2", "Midnight Run", "Electric Pulse", "4:12", 128, "C#m", "Techno"),
        Track("3", "Ocean Breeze", "Lo-Fi Girl", "2:58", 85, "G", "Lo-Fi"),
        Track("4", "Mountain High", "Folk Soul", "3:20", 110, "D", "Folk"),
        Track("5", "Cyber City", "Glitch Hop", "4:05", 140, "Fm", "Electronic"),
        Track("6", "Velvet Night", "Jazz Trio", "5:30", 90, "Bb", "Jazz"),
        Track("7", "Pulse Driver", "DNB King", "4:50", 174, "F", "Drum & Bass"),
        Track("8", "Sunray", "House Vibe", "3:55", 122, "Em", "House"),
        Track("9", "Deep Bass", "Sub Aura", "6:10", 65, "A", "Ambient"),
        Track("10", "Rapid Fire", "Speed Core", "3:15", 185, "C", "Hardcore"),
        Track("11", "Slow Burn", "Blues Legend", "4:45", 72, "E", "Blues"),
        Track("12", "Urban Jungle", "Trap Star", "3:30", 135, "Gm", "Trap"),
        Track("13", "Golden Hour", "Pop Princess", "3:10", 105, "D#", "Pop"),
        Track("14", "Starlight", "Trance Voyager", "7:20", 138, "Bm", "Trance"),
        Track("15", "Retro Vibe", "80s Kid", "3:50", 118, "G#m", "Retrowave")
    )

    fun getDailyMixes(): List<Mix> = listOf(
        Mix("1", "Chill Pulse", "Electronic beats for deep focus.", Color(0xFF6C5CE7)),
        Mix("2", "Energy Flow", "High BPM tracks for your workout.", Color(0xFFD63031)),
        Mix("3", "Midnight Mood", "Smooth synthwave for the night.", Color(0xFF2D3436)),
        Mix("4", "Golden Glow", "Uplifting pop and house vibes.", Color(0xFFFDCB6E))
    )

    fun getGenres(): List<Genre> = listOf(
        Genre("1", "Synthwave", listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))),
        Genre("2", "Lo-Fi", listOf(Color(0xFF3A1C71), Color(0xFFD76D77), Color(0xFFFFAF7B))),
        Genre("3", "Techno", listOf(Color(0xFF141E30), Color(0xFF243B55))),
        Genre("4", "House", listOf(Color(0xFFFF512F), Color(0xFFDD2476))),
        Genre("5", "Jazz", listOf(Color(0xFF000000), Color(0xFF434343))),
        Genre("6", "Pop", listOf(Color(0xFFF093FB), Color(0xFFF5576C)))
    )

    fun getPlaylists(): List<Playlist> = listOf(
        Playlist("1", "Morning Flow", 24, listOf(Color(0xFFFF9A9E), Color(0xFFFAD0C4))),
        Playlist("2", "Beat Match 128", 15, listOf(Color(0xFF84FAB0), Color(0xFF8FD3F4))),
        Playlist("3", "Night Drive", 42, listOf(Color(0xFF30CFD0), Color(0xFF330867))),
        Playlist("4", "Deep Work", 30, listOf(Color(0xFFE2E2E2), Color(0xFFC9D6FF))),
        Playlist("5", "Peak Energy", 12, listOf(Color(0xFFFF0844), Color(0xFFFFB199)))
    )

    fun getActivityHistory(): List<ActivitySession> = listOf(
        ActivitySession("1", "July 30, 2026", 145, "32:45", 4750),
        ActivitySession("2", "July 28, 2026", 120, "15:20", 1820),
        ActivitySession("3", "July 27, 2026", 132, "45:10", 5940),
        ActivitySession("4", "July 25, 2026", 115, "20:00", 2300)
    )
}
