# FlowState 🎧

**FlowState** is a high-fidelity, intelligent music application designed for Android. It prioritizes seamless transitions, intelligent sequencing, and a deep connection between music and physical movement. Built with a modern **Glassmorphism** aesthetic, FlowState offers a premium discovery and playback experience tailored to your rhythm.

---

## 🚀 Key Features

### 1. Intelligent Sequencing
FlowState isn't just a player; it's a conductor. Organize your listening journey using our proprietary BPM-aware sorting algorithms:
- **Ascending/Descending**: Simple BPM-based energy progression.
- **Valley**: Starts high, dips into chill vibes, and builds back up.
- **Peak**: A steady climb to maximum energy.
- **Smart Flow**: An AI-shuffled experience that finds the perfect next track based on key and genre compatibility.

### 2. Activity Mode (Sync to Step)
Experience the ultimate flow state by syncing your music to your movement.
- **Real-time Step Detection**: Uses the device's hardware step counter to calculate your Steps Per Minute (SPM).
- **Dynamic BPM Matching**: Automatically switches to or suggests tracks in your library that match your current physical pace.
- **Live Analytics**: Visualise your rhythm with the real-time **Pace Graph**.

### 3. Immersive Player & Deep Analysis
The heart of the app features a high-fidelity playback environment:
- **Glass Visualizer**: A pulsing, multi-layered ring animation that syncs with the current track's BPM.
- **Powerful Analysis**: Swipe up to reveal technical track metadata, including musical **Key**, **Genre**, and premium insights like **Danceability** and **Flow Stability**.
- **Gesture Controls**: Button-less playback management. Swipe horizontally to skip and vertically to toggle analysis.

### 4. 6 Premium Themes
FlowState adapts to your environment with six distinct, high-fidelity themes:
- **Studio**: Clean, professional, light-focused aesthetic.
- **Club**: Vibrant neon accents for high-energy sessions.
- **Midnight**: Deep blacks and subtle highlights for night-time listening.
- **Vinyl**: Retro-inspired warm tones and classic textures.
- **Prism**: A colorful, multi-gradient glass experience.
- **High Contrast**: Maximum accessibility and sharp definition.

### 5. Seamless Transitions
Engineered for uninterrupted listening:
- **Automatic Crossfades**: Smoothly blends the end of one track into the start of the next.
- **Echo Tails**: Adds a spatial echo to track ends for a natural atmospheric transition.
- **Beat-Matched Blends**: Syncs the outgoing and incoming beats for a professional DJ-style experience.

---

## 🛠 Tech Stack

- **Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose (Declarative UI)
- **Design System**: Material 3 with custom Glassmorphism components.
- **Navigation**: Compose Navigation with a persistent floating navigation architecture.
- **Concurrency**: Kotlin Coroutines & Flow for reactive state management.
- **Sensors**: Android Sensor API (Hardware Step Counter).
- **Haptics**: Advanced VibrationEffect API for theme-matched physical feedback.

---

## 📂 Project Structure

- `ui/theme`: Core design system, color definitions, and the 6-variant theme engine.
- `ui/components`: Reusable UI elements including `GlassBox`, `TrackCard`, and `PaceGraph`.
- `ui/screens`: High-fidelity screen implementations (Onboarding, Home, Library, Activity, Player, Settings).
- `logic`: Core business logic, including `PlaybackManager`, `StepSensorManager`, and `HapticFeedbackManager`.
- `data`: Mock repository and data models for tracks, playlists, and genres.

---

## 📦 Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- Android SDK API 24 (Nougat) or higher (API 31+ recommended for best Glassmorphism effects).
- A physical device with a Step Counter sensor is required for full Activity Mode functionality.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/reeperx/flowstate.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and run the `app` module on your device.

---

## 🗺 Roadmap
- [ ] Integration with real streaming APIs (Spotify/SoundCloud).
- [ ] On-device AI for real-time local file analysis.
- [ ] Expanded Activity Analytics with Weekly Reports.
- [ ] Collaborative Smart Playlists.

---

Developed with ❤️ by the FlowState Team.
