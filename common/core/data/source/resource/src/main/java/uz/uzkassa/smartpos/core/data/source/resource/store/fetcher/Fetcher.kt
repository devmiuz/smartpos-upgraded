package uz.uzkassa.smartpos.core.data.source.resource.store.fetcher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Fetcher<Key : Any, Output : Any?> {

    operator fun invoke(key: Key): Flow<Output>

    companion object {

        fun <Key : Any, Output : Any?> ofFlow(
            flowFactory: (Key) -> Flow<Output>
        ): Fetcher<Key, Output> = FactoryFetcher { key: Key -> flowFactory(key) }

        fun <Key : Any, Output : Any?> of(
            doFetch: suspend (Key) -> Output
        ): Fetcher<Key, Output> =
            ofFlow(doFetch.asFlow())

        private fun <Key, Value> (suspend (key: Key) -> Value).asFlow() =
            { key: Key -> flow { emit(invoke(key)) } }

        private class FactoryFetcher<Key : Any, Output : Any?>(
            private val factory: (Key) -> Flow<Output>
        ) : Fetcher<Key, Output> {
            override fun invoke(key: Key): Flow<Output> =
                factory(key)
        }
    }
}