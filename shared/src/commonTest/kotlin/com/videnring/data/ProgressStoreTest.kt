package com.videnring.data

import com.videnring.model.UserProgress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InMemoryProgressStoreTest {

    @Test
    fun `save and load should return saved progress`() = kotlinx.coroutines.test.runTest {
        val store = InMemoryProgressStore()
        val progress = UserProgress(
            userId = "test-1",
            displayName = "TestUser",
            totalLevel = 5,
            experience = 42,
            battlesWon = 3
        )
        store.save(progress)
        val loaded = store.load()
        assertNotNull(loaded)
        assertEquals("TestUser", loaded.displayName)
        assertEquals(5, loaded.totalLevel)
    }

    @Test
    fun `load should return null when no progress saved`() = kotlinx.coroutines.test.runTest {
        val store = InMemoryProgressStore()
        assertNull(store.load())
    }

    @Test
    fun `clear should remove saved progress`() = kotlinx.coroutines.test.runTest {
        val store = InMemoryProgressStore()
        store.save(UserProgress(userId = "test-2"))
        store.clear()
        assertNull(store.load())
    }

    @Test
    fun `observeProgress should emit saved progress`() = kotlinx.coroutines.test.runTest {
        val store = InMemoryProgressStore()
        val progress = UserProgress(userId = "obs-test", totalLevel = 10)
        store.save(progress)
        val observed = store.observeProgress()
        assertEquals(progress, observed)
    }
}
