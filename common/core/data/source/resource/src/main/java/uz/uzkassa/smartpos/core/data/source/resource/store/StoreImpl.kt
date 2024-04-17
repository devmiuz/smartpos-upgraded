package uz.uzkassa.smartpos.core.data.source.resource.store

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.store.fetcher.Fetcher
import uz.uzkassa.smartpos.core.data.source.resource.store.source.SourceOfTruth

internal class StoreImpl<Key : Any, Input : Any?, Output : Any?>(
    private val fetcher: Fetcher<Key, Input>,
    private val sourceOfTruth: SourceOfTruth<Key, Input, Output>
) : Store<Key, Output> {
    private var sourceOfTruthLock: CompletableDeferred<Unit>? = null

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun stream(request: StoreRequest<Key>): Flow<Output> {
        return when {
            request.refresh -> fetch(request.key)
            else -> sourceOfTruth.reader(request.key)
                .onStart { sourceOfTruthLock?.await(); sourceOfTruthLock = null }
                .let { flow ->
                    flow.onEach { if (it is Collection<*> && it.isEmpty()) throw Error() }
                        .catch {
                            if (it is NoSuchElementException || it is KotlinNullPointerException)
                                emitAll(fetch(request.key, isChild = true))
                            else throw it
                        }
                }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun refreshFlow(key: Key): Flow<Output> {
        return fetch(key)
    }

    override fun storageFlow(key: Key): Flow<Output> {
        return sourceOfTruth.reader(key)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun fetch(key: Key, isChild: Boolean = false): Flow<Output> {
        return fetcher(key)
            .onStart { if (isChild) sourceOfTruthLock = CompletableDeferred() }
            .onEach { if (it != null) sourceOfTruth.write(key, it) }
            .onCompletion { sourceOfTruthLock?.complete(Unit) }
            .flatMapConcat { sourceOfTruth.reader(key) }
    }
}