package uz.uzkassa.smartpos.feature.user.settings.language.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.user.settings.language.data.model.LanguageSelection

internal class LanguageSelectionBroadcastChannel : BroadcastChannelWrapper<LanguageSelection>()