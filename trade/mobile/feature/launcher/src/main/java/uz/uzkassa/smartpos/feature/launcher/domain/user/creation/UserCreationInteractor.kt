package uz.uzkassa.smartpos.feature.launcher.domain.user.creation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.state.LauncherStateRepository
import uz.uzkassa.smartpos.feature.launcher.presentation.navigation.StartScreenType
import javax.inject.Inject

internal class UserCreationInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val currentBranchRepository: CurrentBranchRepository,
    private val launcherStateRepository: LauncherStateRepository
) {

    fun getCurrentBranch(): Flow<Result<Branch>> {
        return currentBranchRepository
            .getCurrentBranch()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun skipUserCreation() =
        launcherStateRepository
            .setLauncherStateByStartScreenType(StartScreenType.USER_CREATION_COMPLETED)
}