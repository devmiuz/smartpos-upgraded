package uz.uzkassa.apay.dependencies

import uz.uzkassa.apay.data.network.rest.ApayRestService
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.ApaySocketService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference

interface CashierApayFeatureDependencies {

    val branchEntityDao: BranchEntityDao

    val currentBranchPreference: CurrentBranchPreference

    val coroutineContextManager: CoroutineContextManager

    val apayRestService: ApayRestService

    val apayFeatureArgs: CashierApayFeatureArgs

    val apayFeatureCallback: CashierApayFeatureCallback

    val apaySocketService: ApaySocketService

    val deviceInfoManager: DeviceInfoManager
}