package uz.uzkassa.smartpos.feature.product.saving.data.channel

import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import java.math.BigDecimal

class ProductVATRateBroadcastChannel : BroadcastChannelWrapper<BigDecimal?>()