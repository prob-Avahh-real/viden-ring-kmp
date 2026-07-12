package com.videnring.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RingSegmentTest {

    @Test
    fun `default segments should have 8 distinct elements`() {
        assertEquals(8, defaultSegments.size)
        val elements = defaultSegments.map { it.element }.distinct()
        assertEquals(8, elements.size)
    }

    @Test
    fun `segment progress should be ratio of level to maxLevel`() {
        val seg = RingSegment("test", "Test", ElementType.Fire, "🔥", 0xFFFFFFFF, 0xFFFF0000, level = 3, maxLevel = 10)
        assertEquals(0.3, seg.progress)
    }

    @Test
    fun `segment progress should be 1 when max level`() {
        val seg = RingSegment("test", "Test", ElementType.Water, "💧", 0xFFFFFFFF, 0xFFFF0000, level = 10, maxLevel = 10)
        assertEquals(1.0, seg.progress)
    }

    @Test
    fun `user progress win rate should handle zero battles`() {
        val p = UserProgress(userId = "test")
        assertEquals(0.0, p.winRate)
    }

    @Test
    fun `user progress win rate should calculate correctly`() {
        val p = UserProgress(userId = "test", battlesWon = 3, battlesLost = 1)
        assertEquals(0.75, p.winRate)
    }

    @Test
    fun `user progress experience progress should be ratio`() {
        val p = UserProgress(userId = "test", experience = 50, experienceToNextLevel = 100)
        assertEquals(0.5, p.experienceProgress)
    }
}
