package uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.BranchRelation
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.terminal.service.TerminalRestService
import uz.uzkassa.smartpos.core.utils.coroutines.flow.switch
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.exception.CurrentBranchNotDefinedException
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.params.CurrentBranchParams
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureArgs
import javax.inject.Inject

internal class BranchSelectionRepositoryImpl @Inject constructor(
    private val branchRelationDao: BranchRelationDao,
    private val branchRestService: BranchRestService,
    private val branchSelectionSetupFeatureArgs: BranchSelectionSetupFeatureArgs,
    private val deviceInfoManager: DeviceInfoManager,
    private val terminalRestService: TerminalRestService,
    private val preferenceCleaner: PreferenceCleaner,
    private val branchEntityDao: BranchEntityDao
    ) : BranchSelectionRepository {
    private val branchId: Long? = branchSelectionSetupFeatureArgs.branchId

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    override fun getCurrentBranch(): Flow<Branch> {
        return flowOf(branchId)
            .flatMapConcat { branchId ->
                return@flatMapConcat if (branchId != null) {
                    flow { emit(branchRelationDao.getRelationFlowByBranchId(branchId).first()) }
                        .switch {
                            return@switch branchRestService
                                .getBranchById(branchId)
                                .flatMapConcat {
                                    val relation: BranchRelation =
                                        branchRelationDao.getRelationFlowByBranchId(branchId)
                                            .first()
                                    return@flatMapConcat flow { emit(relation) }
                                }
                        }
                        .map { it.map() }
                } else throw CurrentBranchNotDefinedException()
            }
    }

    @FlowPreview
    override fun setCurrentBranch(params: CurrentBranchParams): Flow<Long> {
        return flow { emit(deviceInfoManager.deviceInfo) }
            .flatMapConcat {
                terminalRestService
                    .checkTerminalByBranch(
                        branchId = params.id,
                        terminalModel = it.deviceName,
                        terminalSerialNumber = it.serialNumber
                    )
            }
            .map { params.id }
    }

    override fun clearAppDataAndLogout(): Flow<Unit> {
        return flowOf(Unit)
            .onEach {
                branchEntityDao.clearAllTables()
                preferenceCleaner.clearAll()
            }
    }
}
