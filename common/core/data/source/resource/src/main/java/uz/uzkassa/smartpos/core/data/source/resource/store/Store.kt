package uz.uzkassa.smartpos.core.data.source.resource.store

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth

interface Store<Key : Any, Output : Any?> {

    fun refreshFlow(key: Key): Flow<Output>

    fun storageFlow(key: Key): Flow<Output>

    fun stream(request: StoreRequest<Key>): Flow<Output>

    companion object {

        fun <Key : Any, Input : Any?, Output : Any?> from(
            fetcher: Fetcher<Key, Input>,
            sourceOfTruth: SourceOfTruth<Key, Input, Output>
        ): Store<Key, Output> =
            StoreImpl(fetcher, sourceOfTruth)
    }
}