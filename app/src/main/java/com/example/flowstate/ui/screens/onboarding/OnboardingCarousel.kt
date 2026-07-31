package com.example.flowstate.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.flowstate.ui.components.GlassBox
import com.example.flowstate.ui.components.LargeActionButton
import com.example.flowstate.ui.components.OnboardingPagerIndicator
import com.example.flowstate.ui.theme.FlowStateTheme
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OnboardingCarousel(
    onFinished: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            "Intelligent Sequencing",
            "Music that follows your vibe. Sort by BPM, peak energy, or smart flow.",
            Icons.Default.AutoAwesome,
            Color(0xFF6C5CE7)
        ),
        OnboardingPage(
            "Seamless Transitions",
            "No gaps, just flow. Echo tails and beat-matched blends for a perfect mix.",
            Icons.Default.MusicNote,
            Color(0xFF00CEC9)
        ),
        OnboardingPage(
            "Activity Mode",
            "Synced to your movement. Music BPM matches your steps automatically.",
            Icons.Default.DirectionsRun,
            Color(0xFFD63031)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        pages[pagerState.currentPage].color.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { position ->
                val page = pages[position]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GlassBox(
                        modifier = Modifier.size(200.dp),
                        cornerRadius = 0.dp
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                modifier = Modifier.size(100.dp),
                                tint = page.color
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }

            OnboardingPagerIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            LargeActionButton(
                text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinished()
                    }
                }
            )
            
            TextButton(
                onClick = onFinished,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Skip", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingCarouselPreview() {
    FlowStateTheme {
        OnboardingCarousel {}
    }
}
