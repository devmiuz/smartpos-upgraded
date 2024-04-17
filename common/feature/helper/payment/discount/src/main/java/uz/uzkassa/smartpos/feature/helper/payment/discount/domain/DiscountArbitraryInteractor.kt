package uz.uzkassa.smartpos.feature.helper.payment.discount.domain

import android.util.Log
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.utils.math.times
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.channel.DiscountArbitraryResultBroadcastChannel
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountType
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.properties.Delegates

internal class DiscountArbitraryInteractor @Inject constructor(
    private val discountArbitraryBroadcastChannel: DiscountArbitraryBroadcastChannel,
    private val discountArbitraryResultBroadcastChannel: DiscountArbitraryResultBroadcastChannel,
    paymentDiscountFeatureArgs: PaymentDiscountFeatureArgs
) {
    private var _amount: BigDecimal by Delegates.notNull()
    private var _discountAmount: BigDecimal by Delegates.notNull()
    private var _discountPercent: Double by Delegates.notNull()
    private var _discountType: DiscountType by Delegates.notNull()
    private var element: DiscountArbitrary by Delegates.notNull()

    val isDiscountWithPercent: Boolean
        get() = _discountType == DiscountType.BY_PERCENT

    init {
        _amount = paymentDiscountFeatureArgs.amount
        _discountAmount = paymentDiscountFeatureArgs.discountAmount
        _discountPercent = paymentDiscountFeatureArgs.discountPercent
        _discountType = paymentDiscountFeatureArgs.discountType
        Log.wtf("DAInteractor", "init discount type = $_discountType")

        element = getDiscountArbitrary()
        discountArbitraryBroadcastChannel.sendBlocking(element)
    }

    fun getDiscountArbitraryResult(): Flow<Result<Unit>> {
        return flowOf(element)
            .onEach { discountArbitraryResultBroadcastChannel.send(element) }
            .map { Result.success(Unit) }
    }

    fun addAmount(newAmount: BigDecimal) =
        setAmount(_discountAmount + newAmount)

    fun setAmount(newAmount: BigDecimal) {
        _discountType = DiscountType.BY_SUM
        _discountPercent = 0.0
        _discountAmount = when {
            newAmount > BigDecimal.ZERO && newAmount < _amount -> newAmount
            newAmount >= _amount -> _amount
            else -> BigDecimal.ZERO
        }

        element = getDiscountArbitrary()
        discountArbitraryBroadcastChannel.sendBlocking(element)
    }

    fun setPercent(percent: Double) {
        _discountType = DiscountType.BY_PERCENT
        when {
            percent > 0.0 && percent < 100.0 -> {
                _discountAmount = ((_amount * percent) / 100).roundToBigDecimal()
                _discountPercent = percent
            }
            percent >= 100.0 -> {
                _discountAmount = _amount
                _discountPercent = 100.0
            }
            else -> {
                _discountAmount = BigDecimal.ZERO
                _discountPercent = 0.0
            }
        }

        element = getDiscountArbitrary()
        discountArbitraryBroadcastChannel.sendBlocking(element)
    }

    private fun getDiscountArbitrary(): DiscountArbitrary =
        DiscountArbitrary(
            actualAmount = _amount - _discountAmount,
            amount = _amount,
            discountAmount = _discountAmount,
            discountPercent = _discountPercent,
            discountType = _discountType
        )
}