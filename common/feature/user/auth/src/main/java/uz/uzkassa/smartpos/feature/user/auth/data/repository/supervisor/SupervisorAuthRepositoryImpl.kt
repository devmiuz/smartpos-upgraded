package uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.auth.user.service.UserAuthRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole.Type.CASHIER
import uz.uzkassa.smartpos.feature.user.auth.data.repository.shift.ShiftReportRepository
import uz.uzkassa.smartpos.feature.user.auth.data.repository.supervisor.params.SupervisorAuthenticateParams
import uz.uzkassa.smartpos.feature.user.auth.dependencies.UserAuthFeatureArgs
import javax.inject.Inject

internal class SupervisorAuthRepositoryImpl @Inject constructor(
    private val shiftReportRepository: ShiftReportRepository,
    private val userAuthFeatureArgs: UserAuthFeatureArgs,
    private val userAuthRestService: UserAuthRestService
) : SupervisorAuthRepository {

    @FlowPreview
    override fun authenticate(params: SupervisorAuthenticateParams): Flow<Unit> {
        return userAuthRestService.authenticate(params.asJsonElement())
            .flatMapConcat {
                return@flatMapConcat if (userAuthFeatureArgs.userRoleType == CASHIER)
                    shiftReportRepository.openShiftReport(params.userId, params.userName)
                        .catch { throwable ->
                            val logoutFlow: Flow<Unit> =
                                userAuthRestService.logout().onEach { throw throwable }
                            emitAll(logoutFlow)
                        }
                else flowOf(Unit)
            }
    }
}