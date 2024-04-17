package uz.uzkassa.smartpos.core.data.source.resource.store.source;

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class NonFlowingSourceOfTruth<Key, Input, Output>(
    private val realReader: suspend (Key) -> Output,
    private val realWriter: suspend (Key, Input) -> Unit
) : SourceOfTruth<Key, Input, Output> {

    override fun reader(key: Key): Flow<Output> =
        flow { emit(realReader(key)) }

    override suspend fun write(key: Key, value: Input) =
        realWriter(key, value)
}