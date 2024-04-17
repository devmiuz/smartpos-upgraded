package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.user

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.store.StoreRequest
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureArgs
import javax.inject.Inject

internal class  UserRepositoryImpl @Inject constructor(
    cashierSaleFeatureArgs: CashierSaleFeatureArgs,
    private val userStore: UserStore
) : UserRepository {
    private val userId: Long = cashierSaleFeatureArgs.userId

    @FlowPreview
    override fun getCurrentUser(): Flow<User> {
        return userStore.getUserByBranchId().stream(StoreRequest(userId))
    }
}