package uz.uzkassa.smartpos.core.utils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.flatMapResult(): Flow<Result<T>> =
    map { Result.success(it) }.catch { emit(Result.failure(it)) }