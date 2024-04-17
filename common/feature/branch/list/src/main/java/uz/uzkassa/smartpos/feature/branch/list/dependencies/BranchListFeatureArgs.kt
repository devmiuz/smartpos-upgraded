package uz.uzkassa.smartpos.feature.branch.list.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.user.role.model.UserRole
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.branch.list.data.model.confirmation.OwnerConfirmationState

interface BranchListFeatureArgs {

    val branchId: Long

    val ownerConfirmationStateBroadcastChannel: BroadcastChannelWrapper<OwnerConfirmationState>

    val userRoleType: UserRole.Type
}