package uz.uzkassa.smartpos.feature.branch.list.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.branch.list.data.model.branch.BranchWrapper
import uz.uzkassa.smartpos.feature.branch.list.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureArgs
import javax.inject.Inject

internal class BranchListInteractor @Inject constructor(
    private val branchRepository: BranchRepository,
    branchListFeatureArgs: BranchListFeatureArgs,
    private val coroutineContextManager: CoroutineContextManager
) {

    val isBranchCreationAllowed: Boolean =
        branchListFeatureArgs.userRoleType == UserRole.Type.OWNER

    fun getBranches(): Flow<Result<List<BranchWrapper>>> {
        return branchRepository.getBranches()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}