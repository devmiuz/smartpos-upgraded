package uz.uzkassa.smartpos.feature.branch.saving.domain.update

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.BranchSavingRepository
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.params.SaveBranchParams
import uz.uzkassa.smartpos.feature.branch.saving.domain.BranchSaveInteractor
import javax.inject.Inject
import kotlin.properties.Delegates

internal class BranchUpdateInteractor @Inject constructor(
    private val branchRepository: BranchRepository,
    private val branchSavingRepository: BranchSavingRepository,
    private val coroutineContextManager: CoroutineContextManager
) : BranchSaveInteractor() {
    private var branchId: Long by Delegates.notNull()

    fun getBranch(): Flow<Result<Branch>> {
        return branchRepository
            .getBranch()
            .onEach { it ->
                branchId = it.id
                it.activityType?.let { setActivityType(it) }
                it.region?.let { setRegion(it) }
                it.city?.let { setCity(it) }
                setName(it.name)
                it.address?.let { setAddress(it) }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun updateBranch(): Flow<Result<Unit>> =
        proceedWithResult { activityType, name, region, city, address ->
            branchSavingRepository
                .updateBranch(
                    SaveBranchParams(
                        id = branchId,
                        activityType = activityType,
                        name = name,
                        region = region,
                        city = city,
                        address = address
                    )
                )
                .map { Unit }
                .flowOn(coroutineContextManager.ioContext)
        }
}