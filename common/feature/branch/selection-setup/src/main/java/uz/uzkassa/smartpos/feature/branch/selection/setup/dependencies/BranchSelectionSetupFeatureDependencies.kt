package uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.terminal.service.TerminalRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager


interface BranchSelectionSetupFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val branchRelationDao: BranchRelationDao

    val branchRestService: BranchRestService

    val branchSelectionSetupFeatureArgs: BranchSelectionSetupFeatureArgs

    val branchSelectionSetupFeatureCallback: BranchSelectionSetupFeatureCallback

    val coroutineContextManager: CoroutineContextManager

    val deviceInfoManager: DeviceInfoManager

    val terminalRestService: TerminalRestService

    val preferenceCleaner: PreferenceCleaner

}