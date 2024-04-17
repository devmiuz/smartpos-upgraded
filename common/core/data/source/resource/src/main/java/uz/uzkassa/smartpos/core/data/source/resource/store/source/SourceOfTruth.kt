package uz.uzkassa.smartpos.core.data.source.resource.store.source;

import kotlinx.coroutines.flow.Flow

interface SourceOfTruth<Key, Input: Any?, Output : Any?> {

    fun reader(key: Key): Flow<Output>

    suspend fun write(key: Key, value: Input)

    companion object {

        fun <Key : Any, Input : Any?, Output : Any?> of(
            nonFlowReader: suspend (Key) -> Output,
            writer: suspend (Key, Input) -> Unit
        ): SourceOfTruth<Key, Input, Output> =
            NonFlowingSourceOfTruth(nonFlowReader, writer)

        fun <Key : Any, Input : Any?, Output : Any?> of(
            reader: (Key) -> Flow<Output>,
            writer: suspend (Key, Input) -> Unit
        ): SourceOfTruth<Key, Input, Output> =
            FlowingSourceOfTruth(reader, writer)
    }
}