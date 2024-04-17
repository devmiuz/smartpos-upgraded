package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.user

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.store.StoreRequest
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs,
    private val userAuthRestService: UserAuthRestService,
    private val userStore: UserStore
) : UserRepository {
    private val userId: Long = supervisorDashboardFeatureArgs.userId

    @FlowPreview
    override fun getUser(): Flow<User> {
        return userStore.getUserByBranchId()
            .stream(StoreRequest(userId))
    }

    override fun logoutUser(): Flow<Unit> {
        return userAuthRestService.logout()
    }
}