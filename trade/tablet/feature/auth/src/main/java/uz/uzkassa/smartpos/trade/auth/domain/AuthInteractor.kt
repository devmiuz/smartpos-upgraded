package uz.uzkassa.smartpos.trade.auth.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import javax.inject.Inject

internal class AuthInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager
) {

    fun getAuthCode(): Flow<Result<String>> {
        return flowOf(Result.success("Test"))

    }

    @FlowPreview
    fun checkCode(): Flow<Result<Unit>> {
        return flowOf(Result.success(Unit))
            .debounce(1000)
    }
}