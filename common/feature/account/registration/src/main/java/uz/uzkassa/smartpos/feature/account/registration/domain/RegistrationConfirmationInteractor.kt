package uz.uzkassa.smartpos.feature.account.registration.domain

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.flow.flowInterval
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.account.registration.data.model.countdown.ConfirmationCountdown
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class RegistrationConfirmationInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager
) {

    fun setActivationCode(value: String): Flow<Boolean> {
        return flowOf(TextUtils.replaceAllLetters(value).length == 6)
    }

    @ObsoleteCoroutinesApi
    fun requestCountdownForResend(): Flow<ConfirmationCountdown> {
        return flowInterval(60, 1, TimeUnit.SECONDS)
            .map { ConfirmationCountdown(60, 60 - it, it == 60L) }
            .flowOn(coroutineContextManager.ioContext)
    }
}