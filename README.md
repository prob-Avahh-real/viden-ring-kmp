# Viden Ring — KMP

An elemental ring knowledge/sharing app built with Kotlin Multiplatform + Compose Multiplatform.

Master 8 elements (Fire, Water, Earth, Air, Light, Shadow, Arcane, Nature) on a mystical ring interface. Tap segments to view details, level up abilities, and track your progress.

## Architecture

```
model/          → RingSegment, UserProgress, ElementType (shared)
engine/         → RingEngine (game logic, XP, leveling)
data/           → IProgressStore + InMemoryProgressStore
composeApp/     → App entry, RingScreen UI (Canvas-based ring, detail cards)
```

## Build

```bash
./gradlew :shared:allTests          # Run all tests
./gradlew :composeApp:assembleRelease  # Build Android APK
./gradlew :shared:linkDebugFrameworkIosArm64  # Build iOS framework
```

## CI/CD

GitHub Actions: lint → test → android-build → ios-build

## Tech Stack

- Kotlin 2.1.10, Compose Multiplatform 1.7.3
- Material3, kotlinx-coroutines, kotlinx-datetime
- ProGuard minification for release builds
