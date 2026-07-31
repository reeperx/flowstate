package com.example.flowstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flowstate.data.MusicRepository
import com.example.flowstate.data.Track
import com.example.flowstate.logic.*
import com.example.flowstate.ui.components.*
import com.example.flowstate.ui.screens.*
import com.example.flowstate.ui.screens.onboarding.OnboardingAuth
import com.example.flowstate.ui.screens.onboarding.OnboardingCarousel
import com.example.flowstate.ui.screens.onboarding.OnboardingPermissions
import com.example.flowstate.ui.screens.onboarding.OnboardingThemeSetup
import com.example.flowstate.ui.theme.AppTheme
import com.example.flowstate.ui.theme.FlowStateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val playbackManager = PlaybackManager()
        val hapticManager = HapticFeedbackManager(this)
        val stepManager = StepSensorManager(this)
        
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.Studio) }
            
            // Handle Activity Recognition Permission
            LaunchedEffect(Unit) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    if (checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 100)
                    }
                }
            }

            CompositionLocalProvider(
                LocalPlaybackManager provides playbackManager,
                LocalStepSensorManager provides stepManager
            ) {
                val spm by stepManager.spm.collectAsState()
                
                // BPM Matching Logic
                LaunchedEffect(spm) {
                    if (spm > 0) {
                        playbackManager.findAndPlayMatchingBpm(spm)
                    }
                }

                FlowStateTheme(appTheme = currentTheme) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val currentTrack = playbackManager.currentTrack

                    val bgBrush = remember(currentTheme) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    }
                    
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            AppBottomBar(
                                currentRoute = currentRoute,
                                currentTrack = currentTrack,
                                playbackManager = playbackManager,
                                hapticManager = hapticManager,
                                currentTheme = currentTheme,
                                navController = navController
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize().background(bgBrush)) {
                            NavHost(
                                navController = navController,
                                startDestination = "onboarding/carousel",
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable("onboarding/carousel") {
                                    OnboardingCarousel(
                                        onFinished = { navController.navigate("onboarding/theme") }
                                    )
                                }
                                composable("onboarding/theme") {
                                    OnboardingThemeSetup(
                                        currentTheme = currentTheme,
                                        onThemeChange = { currentTheme = it },
                                        onContinue = { navController.navigate("onboarding/permissions") }
                                    )
                                }
                                composable("onboarding/permissions") {
                                    OnboardingPermissions(
                                        onGrant = {
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                                requestPermissions(arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 100)
                                            }
                                            navController.navigate("onboarding/auth")
                                        },
                                        onSkip = { navController.navigate("onboarding/auth") }
                                    )
                                }
                                composable("onboarding/auth") {
                                    OnboardingAuth(
                                        onFinished = {
                                            navController.navigate("home") {
                                                popUpTo("onboarding/carousel") { inclusive = true }
                                            }
                                        }
                                    )
                                }
                                composable("home") {
                                    Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                                        HomeScreen(
                                            onTrackClick = { track ->
                                                playbackManager.playTrack(track)
                                                navController.navigate("player")
                                            }
                                        )
                                    }
                                }
                                composable("library") {
                                    Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                                        LibraryScreen()
                                    }
                                }
                                composable("activity") {
                                    Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                                        ActivityScreen()
                                    }
                                }
                                composable("player") {
                                    PlayerScreen(
                                        onBack = { navController.popBackStack() },
                                        onHaptic = { hapticManager.performHaptic(currentTheme) }
                                    )
                                }
                                composable("settings") {
                                    Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                                        SettingsScreen(
                                            currentTheme = currentTheme,
                                            onThemeChange = { theme ->
                                                currentTheme = theme
                                                hapticManager.performHaptic(currentTheme)
                                            },
                                            onLogout = {
                                                navController.navigate("onboarding/carousel") {
                                                    popUpTo(0)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomBar(
    currentRoute: String?,
    currentTrack: Track?,
    playbackManager: PlaybackManager,
    hapticManager: HapticFeedbackManager,
    currentTheme: AppTheme,
    navController: NavController
) {
    if (currentRoute != null && !currentRoute.startsWith("onboarding") && currentRoute != "player") {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (currentTrack != null && !playbackManager.isMiniPlayerDismissed) {
                    MiniPlayer(
                        track = currentTrack,
                        isPlaying = playbackManager.isPlaying,
                        onTogglePlay = { 
                            playbackManager.togglePlayback()
                            hapticManager.performHaptic(currentTheme)
                        },
                        onClick = { navController.navigate("player") },
                        onDismiss = { playbackManager.dismissMiniPlayer() }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                FloatingNavBar {
                    NavBarItem(
                        icon = Icons.Default.Home,
                        label = "Home",
                        isSelected = currentRoute == "home",
                        onClick = { 
                            navController.navigate("home") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    NavBarItem(
                        icon = Icons.Default.LibraryMusic,
                        label = "Library",
                        isSelected = currentRoute == "library",
                        onClick = { 
                            navController.navigate("library") {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavBarItem(
                        icon = Icons.Default.DirectionsRun,
                        label = "Activity",
                        isSelected = currentRoute == "activity",
                        onClick = { 
                            navController.navigate("activity") {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavBarItem(
                        icon = Icons.Default.Person,
                        label = "Profile",
                        isSelected = currentRoute == "settings",
                        onClick = { 
                            navController.navigate("settings") {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}
