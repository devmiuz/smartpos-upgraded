package uz.uzkassa.smartpos.feature.helper.payment.amount.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.Amount
import uz.uzkassa.smartpos.feature.helper.payment.amount.data.model.AmountType
import uz.uzkassa.smartpos.feature.helper.payment.amount.dependencies.PaymentAmountFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
internal class AmountInteractor @Inject constructor(
    paymentAmountFeatureArgs: PaymentAmountFeatureArgs
) {
    private var allowedAmount: BigDecimal? = null
    private var leftAmount: BigDecimal
    private var totalAmount: BigDecimal
    private var element: Amount

    private var calculateActualData: Boolean = false

    val amount: Amount
        get() = element

    init {
        val amount: BigDecimal =
            if (!calculateActualData) BigDecimal.ZERO
            else paymentAmountFeatureArgs.amount
        val type: AmountType = paymentAmountFeatureArgs.amountType
        paymentAmountFeatureArgs.allowedAmount?.let { allowedAmount = it }
        totalAmount = paymentAmountFeatureArgs.totalAmount
        leftAmount =
            if (!calculateActualData) totalAmount - paymentAmountFeatureArgs.amount
            else paymentAmountFeatureArgs.leftAmount
        element = Amount(
            amount,
            getChangeAmount(amount),
            getLeftAmount(amount),
            totalAmount,
            type,
            paymentAmountFeatureArgs.creditAdvanceHolder
        )
    }

    fun addAmount(value: String): Amount {
        val currentAmount: BigDecimal = element.amount
        val newAmount: Amount = setAmount(value)
        val result: BigDecimal = currentAmount + newAmount.amount
        return setAmount(result)
    }

    fun setAmount(value: String): Amount =
        setAmount(runCatching { value.toBigDecimal() }.getOrDefault(BigDecimal.ZERO))

    fun setAmount(value: BigDecimal): Amount {
        val allowedAmount: BigDecimal? = allowedAmount
        val changeAmount: BigDecimal = getChangeAmount(value)
        val leftAmount: BigDecimal = getLeftAmount(value)

        if (allowedAmount != null && value > allowedAmount) {
            element = element.copy(
                amount = allowedAmount,
                changeAmount = BigDecimal.ZERO,
                leftAmount = BigDecimal.ZERO
            )

            return element
        }

        if (changeAmount > BigDecimal.ZERO) {
            if (element.type == AmountType.CARD || allowedAmount != null) {
                return setAmount(this.leftAmount)
            }
        }

        return if (allowedAmount != null && allowedAmount < value) element
        else {
            element = element.copy(
                amount = value,
                changeAmount = changeAmount,
                leftAmount = leftAmount
            )

            element
        }
    }

    fun setLeftAmount(): Amount =
        setAmount(leftAmount)

    fun proceedCreditAdvanceReceipt(): Flow<Result<Amount>> {
        return if (element.creditAdvanceHolder != null) {
            getAmountResult()
        } else {
            flowOf()
        }
    }

    fun getAmountResult(): Flow<Result<Amount>> =
        when {
            element.amount > BigDecimal.ZERO -> {
                flowOf(element)
                    .map { Result.success(it) }
            }
            element.amount == BigDecimal.ZERO -> {
                setLeftAmount()
                flowOf(element)
                    .map { Result.success(it) }
            }
            else -> {
                flowOf(Result.failure(RuntimeException()))
            }
        }


    private fun getChangeAmount(amount: BigDecimal): BigDecimal {
        return when {
            leftAmount - amount >= BigDecimal.ZERO -> BigDecimal.ZERO
            else -> amount - leftAmount
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun getLeftAmount(amount: BigDecimal): BigDecimal {
        if (!calculateActualData)
            return (allowedAmount?.minus(amount) ?: leftAmount - amount)
                .let { if (it >= BigDecimal.ZERO) it else BigDecimal.ZERO }

        val allowedAmount: BigDecimal? = allowedAmount
        val leftAmount: BigDecimal = allowedAmount?.minus(amount) ?: leftAmount - amount
        val result: BigDecimal = when {
            leftAmount > BigDecimal.ZERO -> leftAmount
            else -> BigDecimal.ZERO
        }

        return if (allowedAmount != null && allowedAmount < result) allowedAmount
        else result
    }
}