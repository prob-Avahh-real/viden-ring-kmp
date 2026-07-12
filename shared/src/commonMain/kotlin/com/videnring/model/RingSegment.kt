// RingSegment.kt — viden-ring elemental progress model
package com.videnring.model

import androidx.compose.ui.graphics.Color

enum class ElementType { Fire, Water, Earth, Air, Light, Shadow, Arcane, Nature }

data class RingSegment(
    val id: String,
    val name: String,
    val element: ElementType,
    val icon: String,       // emoji icon string
    val color: Long,        // ARGB color value
    val glowColor: Long,
    val level: Int = 1,
    val maxLevel: Int = 10,
    val power: Double = 0.0,
    val description: String = "",
    val abilities: List<String> = emptyList()
) {
    val progress: Double get() = level.toDouble() / maxLevel
}

val defaultSegments = listOf(
    RingSegment("fire", "Flame", ElementType.Fire, "🔥",
        0xFFD94A4A, 0xFFFF6B35,
        description = "Harness the power of flames to burn through obstacles.",
        abilities = listOf("Fireball", "Inferno", "Heat Wave")),
    RingSegment("water", "Tide", ElementType.Water, "💧",
        0xFF4A90D9, 0xFF35B4FF,
        description = "Control the flow of water to heal and protect.",
        abilities = listOf("Healing Rain", "Tidal Wave", "Ice Shard")),
    RingSegment("earth", "Stone", ElementType.Earth, "⛰",
        0xFF8B6914, 0xFFBFA04A,
        description = "Command the earth to fortify defenses.",
        abilities = listOf("Stone Wall", "Quake", "Crystal Armor")),
    RingSegment("air", "Storm", ElementType.Air, "🌪",
        0xFFB0B8C8, 0xFFE0E8F8,
        description = "Bend the winds to strike with speed.",
        abilities = listOf("Gust", "Lightning Strike", "Tornado")),
    RingSegment("light", "Radiance", ElementType.Light, "☀",
        0xFFF0D68A, 0xFFFFF5C0,
        description = "Channel pure light to illuminate and blind foes.",
        abilities = listOf("Flash", "Holy Beam", "Blessing")),
    RingSegment("shadow", "Void", ElementType.Shadow, "🌑",
        0xFF6A4A8B, 0xFF9B6AD9,
        description = "Wield darkness to deceive and drain.",
        abilities = listOf("Shadow Step", "Drain Life", "Nightfall")),
    RingSegment("arcane", "Mystic", ElementType.Arcane, "✨",
        0xFF9B4AD9, 0xFFC06AFF,
        description = "Tap into arcane energies for raw magical power.",
        abilities = listOf("Arcane Missile", "Time Warp", "Mana Surge")),
    RingSegment("nature", "Grove", ElementType.Nature, "🌳",
        0xFF4AD98B, 0xFF6AFFA8,
        description = "Commune with nature to grow and restore.",
        abilities = listOf("Thorn Whip", "Regrowth", "Entangle"))
)

data class UserProgress(
    val userId: String,
    val displayName: String = "Seeker",
    val totalLevel: Int = 1,
    val experience: Int = 0,
    val experienceToNextLevel: Int = 100,
    val achievements: Int = 0,
    val battlesWon: Int = 0,
    val battlesLost: Int = 0,
    val lastLogin: Long = 0L, // epoch millis
    val segmentLevels: Map<String, Int> = emptyMap(),
    val segmentPowers: Map<String, Double> = emptyMap()
) {
    val experienceProgress: Double get() = experience.toDouble() / experienceToNextLevel
    val winRate: Double get() =
        if (battlesWon + battlesLost > 0) battlesWon.toDouble() / (battlesWon + battlesLost) else 0.0
}
