package uz.uzkassa.smartpos.feature.branch.selection.setup.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection

internal class BranchSelectionBroadcastChannel : BroadcastChannelWrapper<BranchSelection>()