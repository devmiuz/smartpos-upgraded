package uz.uzkassa.smartpos.core.manager.coroutine

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal class CoroutineContextManagerImpl : CoroutineContextManager {
    override val defaultContext: CoroutineContext
        get() = Dispatchers.Default

    override val ioContext: CoroutineContext
        get() = Dispatchers.IO

    override val mainContext: CoroutineContext
        get() = Dispatchers.Main.immediate

    override val unconfinedContext: CoroutineContext
        get() = Dispatchers.Unconfined
}