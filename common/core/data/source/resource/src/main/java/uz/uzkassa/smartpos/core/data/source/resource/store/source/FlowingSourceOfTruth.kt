package uz.uzkassa.smartpos.core.data.source.resource.store.source

import kotlinx.coroutines.flow.Flow

internal class FlowingSourceOfTruth<Key, Input, Output>(
    private val realReader: (Key) -> Flow<Output>,
    private val realWriter: suspend (Key, Input) -> Unit
) : SourceOfTruth<Key, Input, Output> {

    override fun reader(key: Key): Flow<Output> =
        realReader(key)

    override suspend fun write(key: Key, value: Input) =
        realWriter(key, value)
}