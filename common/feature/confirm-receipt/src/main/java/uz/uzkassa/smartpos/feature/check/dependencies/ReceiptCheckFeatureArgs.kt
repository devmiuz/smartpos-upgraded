package uz.uzkassa.smartpos.feature.check.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper

interface ReceiptCheckFeatureArgs {
    val branchId: Long

    val userRoleType: UserRole.Type

    val qrUrlBroadcastChannel: BroadcastChannelWrapper<String>
}