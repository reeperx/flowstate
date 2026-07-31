package com.example.flowstate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flowstate.ui.components.GlassBox
import com.example.flowstate.ui.components.SettingsItem
import com.example.flowstate.ui.components.SettingsSection
import com.example.flowstate.ui.theme.AppTheme
import com.example.flowstate.ui.theme.FlowStateTheme

@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onLogout: () -> Unit
) {
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
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader()
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SettingsSection(title = "Account") {
                        SettingsItem(
                            icon = Icons.Default.Person,
                            title = "Personal Information",
                            subtitle = "Name, Email, Phone"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        SettingsItem(
                            icon = Icons.Default.Security,
                            title = "Security & Password",
                            subtitle = "2FA, Recovery options"
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        SettingsItem(
                            icon = Icons.Default.Share,
                            title = "Social Connections",
                            subtitle = "Google, Apple, Facebook"
                        )
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SettingsSection(title = "App Appearance") {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Theme Selection",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // 3x2 Grid for themes
                            val themes = AppTheme.entries
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                themes.chunked(3).forEach { rowThemes ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowThemes.forEach { theme ->
                                            Box(modifier = Modifier.weight(1f)) {
                                                ThemeChip(
                                                    theme = theme,
                                                    isSelected = currentTheme == theme,
                                                    onClick = { onThemeChange(theme) }
                                                )
                                            }
                                        }
                                        // Fill empty spaces in last row if needed
                                        if (rowThemes.size < 3) {
                                            repeat(3 - rowThemes.size) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        var highContrast by remember { mutableStateOf(false) }
                        SettingsItem(
                            icon = Icons.Default.Contrast,
                            title = "High Contrast Mode",
                            trailing = {
                                Switch(
                                    checked = highContrast,
                                    onCheckedChange = { highContrast = it }
                                )
                            }
                        )
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SettingsSection(title = "Playback Settings") {
                        var seamless by remember { mutableStateOf(true) }
                        SettingsItem(
                            icon = Icons.Default.Tune,
                            title = "Seamless Transitions",
                            subtitle = "Echo tails and beat-matching",
                            trailing = {
                                Switch(
                                    checked = seamless,
                                    onCheckedChange = { seamless = it }
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        var haptics by remember { mutableStateOf(true) }
                        SettingsItem(
                            icon = Icons.Default.Vibration,
                            title = "Haptic Feedback",
                            subtitle = "Theme-matched vibrations",
                            trailing = {
                                Switch(
                                    checked = haptics,
                                    onCheckedChange = { haptics = it }
                                )
                            }
                        )
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SettingsSection(title = "Support & Legal") {
                        SettingsItem(icon = Icons.Default.PrivacyTip, title = "Privacy Policy")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        SettingsItem(icon = Icons.Default.Description, title = "Terms of Service")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        SettingsItem(icon = Icons.Default.Info, title = "About FlowState", subtitle = "Version 1.0.0")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Alex Flow",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Premium Member • alex@flowstate.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ThemeChip(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        cornerRadius = 12.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = theme.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
