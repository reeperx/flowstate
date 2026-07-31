package com.example.flowstate.logic

import androidx.compose.runtime.*
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.data.Track

class PlaybackManager {
    var currentTrack by mutableStateOf<Track?>(null)
        private set
        
    var isPlaying by mutableStateOf(false)
        private set

    var isMiniPlayerDismissed by mutableStateOf(false)

    fun playTrack(track: Track) {
        currentTrack = track
        isPlaying = true
        isMiniPlayerDismissed = false
    }

    fun togglePlayback() {
        isPlaying = !isPlaying
    }
    
    fun dismissMiniPlayer() {
        isMiniPlayerDismissed = true
    }
    
    fun nextTrack() {
        val tracks = MusicRepository.getDummyTracks()
        val currentIndex = tracks.indexOfFirst { it.id == currentTrack?.id }
        if (currentIndex != -1) {
            currentTrack = tracks[(currentIndex + 1) % tracks.size]
            isMiniPlayerDismissed = false
        }
    }
    
    fun previousTrack() {
        val tracks = MusicRepository.getDummyTracks()
        val currentIndex = tracks.indexOfFirst { it.id == currentTrack?.id }
        if (currentIndex != -1) {
            currentTrack = tracks[if (currentIndex > 0) currentIndex - 1 else tracks.size - 1]
            isMiniPlayerDismissed = false
        }
    }

    fun findAndPlayMatchingBpm(targetBpm: Int) {
        if (targetBpm <= 0) return
        val tracks = MusicRepository.getDummyTracks()
        val bestMatch = tracks.minByOrNull { kotlin.math.abs(it.bpm - targetBpm) }
        bestMatch?.let {
            if (it.id != currentTrack?.id) {
                playTrack(it)
            }
        }
    }
}

val LocalPlaybackManager = staticCompositionLocalOf { PlaybackManager() }
