package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user.session

import kotlinx.coroutines.flow.Flow

internal interface UserSessionRepository {

    fun pauseSession(): Flow<Unit>

    fun logout(): Flow<Unit>
}