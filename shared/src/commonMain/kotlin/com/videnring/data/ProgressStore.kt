package com.videnring.data

import com.videnring.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface IProgressStore {
    fun observeProgress(): Flow<UserProgress>
    suspend fun save(progress: UserProgress)
    suspend fun load(): UserProgress?
    suspend fun clear()
}

class InMemoryProgressStore : IProgressStore {
    private val mutex = Mutex()
    private val _progress = MutableStateFlow<UserProgress?>(null)
    override fun observeProgress(): Flow<UserProgress> = _progress.asStateFlow()

    override suspend fun save(progress: UserProgress) = mutex.withLock {
        _progress.value = progress
    }

    override suspend fun load(): UserProgress? = mutex.withLock {
        _progress.value
    }

    override suspend fun clear() = mutex.withLock {
        _progress.value = null
    }
}
