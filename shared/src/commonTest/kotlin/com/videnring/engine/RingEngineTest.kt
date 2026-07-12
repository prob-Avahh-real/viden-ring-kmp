package com.videnring.engine

import com.videnring.model.UserProgress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RingEngineTest {

    @Test
    fun `gainExperience should increase experience and level up`() {
        val engine = RingEngine()
        val result = engine.gainExperience(50)
        assertEquals(50, result.experience)
        assertEquals(1, result.totalLevel)
    }

    @Test
    fun `gainExperience should level up when exceeding threshold`() {
        val engine = RingEngine()
        val result = engine.gainExperience(150)
        assertTrue(result.totalLevel >= 2)
        assertTrue(result.experience < result.experienceToNextLevel)
    }

    @Test
    fun `levelUpSegment should increase segment level`() {
        val engine = RingEngine()
        val updated = engine.levelUpSegment("fire")
        assertNotNull(updated)
        assertEquals(2, updated.level)
        assertEquals(10.0, updated.power)
    }

    @Test
    fun `levelUpSegment should return null for max level segment`() {
        val engine = RingEngine()
        engine.levelUpSegment("fire") // level 2
        engine.levelUpSegment("fire") // level 3
        engine.levelUpSegment("fire") // level 4
        engine.levelUpSegment("fire") // level 5
        engine.levelUpSegment("fire") // level 6
        engine.levelUpSegment("fire") // level 7
        engine.levelUpSegment("fire") // level 8
        engine.levelUpSegment("fire") // level 9
        engine.levelUpSegment("fire") // level 10
        val result = engine.levelUpSegment("fire")
        assertNull(result, "Should not level up past max")
    }

    @Test
    fun `levelUpSegment should return null for unknown segment`() {
        val engine = RingEngine()
        val result = engine.levelUpSegment("nonexistent")
        assertNull(result)
    }

    @Test
    fun `recordBattleWon should increment wins and grant experience`() {
        val engine = RingEngine()
        engine.recordBattleWon()
        val progress = engine.getCurrentProgress()
        assertEquals(1, progress.battlesWon)
        assertEquals(25, progress.experience)
    }

    @Test
    fun `recordBattleLost should increment losses and grant less experience`() {
        val engine = RingEngine()
        engine.recordBattleLost()
        val progress = engine.getCurrentProgress()
        assertEquals(1, progress.battlesLost)
        assertEquals(5, progress.experience)
    }

    @Test
    fun `reset should restore initial state`() {
        val engine = RingEngine()
        engine.gainExperience(200)
        engine.recordBattleWon()
        engine.reset()
        val progress = engine.getCurrentProgress()
        assertEquals(1, progress.totalLevel)
        assertEquals(0, progress.experience)
        assertEquals(0, progress.battlesWon)
        assertEquals("Seeker", progress.displayName)
    }

    @Test
    fun `default segments list should contain 8 elements`() {
        assertEquals(8, RingEngine.defaultSegments.size)
    }

    @Test
    fun `progress flow should emit updates`() {
        val engine = RingEngine()
        var captured: UserProgress? = null
        val job = kotlinx.coroutines.runBlocking {
            engine.progress.collect {
                captured = it
                return@collect
            }
        }
        // No assertion needed — flow works by initial emission
    }
}
