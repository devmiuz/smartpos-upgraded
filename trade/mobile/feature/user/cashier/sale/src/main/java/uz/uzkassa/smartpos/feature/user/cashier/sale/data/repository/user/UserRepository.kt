package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

interface UserRepository {

    fun getCurrentUser(): Flow<User>
}