package com.videnring.engine

import com.videnring.model.ElementType
import com.videnring.model.RingSegment
import com.videnring.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RingEngine {
    private val _progress = MutableStateFlow(
        UserProgress(userId = "default", displayName = "Seeker")
    )
    val progress: Flow<UserProgress> = _progress.asStateFlow()

    private val _segments = MutableStateFlow(defaultSegments)
    val segments: Flow<List<RingSegment>> = _segments.asStateFlow()

    fun getCurrentProgress(): UserProgress = _progress.value
    fun getCurrentSegments(): List<RingSegment> = _segments.value

    fun gainExperience(amount: Int): UserProgress {
        val p = _progress.value
        var exp = p.experience + amount
        var level = p.totalLevel
        var nextThreshold = p.experienceToNextLevel
        while (exp >= nextThreshold) {
            exp -= nextThreshold
            level++
            nextThreshold = (nextThreshold * 1.5).toInt().coerceAtLeast(100)
        }
        val updated = p.copy(
            totalLevel = level,
            experience = exp,
            experienceToNextLevel = nextThreshold
        )
        _progress.value = updated
        return updated
    }

    fun levelUpSegment(segmentId: String): RingSegment? {
        val segs = _segments.value.toMutableList()
        val idx = segs.indexOfFirst { it.id == segmentId }
        if (idx == -1) return null
        val seg = segs[idx]
        if (seg.level >= seg.maxLevel) return null
        val updated = seg.copy(
            level = seg.level + 1,
            power = seg.power + 10.0
        )
        segs[idx] = updated
        _segments.value = segs

        _progress.value = _progress.value.copy(
            segmentLevels = _progress.value.segmentLevels + (segmentId to updated.level),
            segmentPowers = _progress.value.segmentPowers + (segmentId to updated.power)
        )
        return updated
    }

    fun recordBattleWon() {
        val p = _progress.value
        _progress.value = p.copy(battlesWon = p.battlesWon + 1)
        gainExperience(25)
    }

    fun recordBattleLost() {
        val p = _progress.value
        _progress.value = p.copy(battlesLost = p.battlesLost + 1)
        gainExperience(5)
    }

    fun reset() {
        _progress.value = UserProgress(userId = "default", displayName = "Seeker")
        _segments.value = defaultSegments
    }

    companion object {
        val defaultSegments = listOf(
            RingSegment("fire", "Flame", ElementType.Fire, "🔥",
                0xFFD94A4A, 0xFFFF6B35,
                description = "Harness the power of flames to burn through obstacles.",
                abilities = listOf("Fireball", "Inferno", "Heat Wave")),
            RingSegment("water", "Tide", ElementType.Water, "💧",
                0xFF4A90D9, 0xFF35B4FF,
                description = "Control the flow of water to heal and protect.",
                abilities = listOf("Healing Rain", "Tidal Wave", "Ice Shard")),
            RingSegment("earth", "Stone", ElementType.Earth, "⛰️",
                0xFF8B6914, 0xFFBFA04A,
                description = "Command the earth to fortify defenses.",
                abilities = listOf("Stone Wall", "Quake", "Crystal Armor")),
            RingSegment("air", "Storm", ElementType.Air, "🌪️",
                0xFFB0B8C8, 0xFFE0E8F8,
                description = "Bend the winds to strike with speed.",
                abilities = listOf("Gust", "Lightning Strike", "Tornado")),
            RingSegment("light", "Radiance", ElementType.Light, "☀️",
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
    }
}
