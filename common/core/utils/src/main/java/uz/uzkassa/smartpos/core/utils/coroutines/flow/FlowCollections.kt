package uz.uzkassa.smartpos.core.utils.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onEach

inline fun <T : List<T>> Flow<T>.switch(crossinline flow: () -> Flow<T>): Flow<T> =
    onEach { if (it.isEmpty()) throw NoSuchElementException() }.catch { emitAll(flow()) }