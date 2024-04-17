package uz.uzkassa.smartpos.feature.helper.product.quantity.data.channel

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.math.times
import uz.uzkassa.smartpos.core.utils.primitives.round
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import java.math.BigDecimal
import kotlin.properties.Delegates
import kotlinx.coroutines.flow.asFlow as asFlowExt

internal class ProductQuantityBroadcastChannel : BroadcastChannelWrapper<ProductQuantity>() {
    private val productUnits: MutableList<ProductUnit> = arrayListOf()
    private var amount: BigDecimal by Delegates.notNull()
    private var maxQuantity: Double by Delegates.notNull()
    private var lastUnitId: Long? = null
    private var element: ProductQuantity by Delegates.notNull()
    private var isQuantity: Boolean = false

    fun setData(lastUnitId: Long?, amount: BigDecimal, quantity: Double, maxQuantity: Double) {
        val actualAmount: BigDecimal =
            if (quantity == 0.0) BigDecimal.ZERO
            else (amount * quantity).roundToBigDecimal()

        this.amount = amount
        this.maxQuantity = maxQuantity
        this.lastUnitId = lastUnitId
        this.element = ProductQuantity(actualAmount, quantity, maxQuantity)
        offer(element.amount, element.quantity)
    }

    fun setProductUnit(productUnit: ProductUnit) {
        element = calculateProductQuantity(element, productUnit)
        offer(element.amount, element.quantity)
    }

    fun setProductUnits(productUnits: List<ProductUnit>) {
        productUnits.find { it.isBase }?.price?.let { amount = it }
        this.productUnits.let { it.clear(); return@let it.addAll(productUnits) }
    }

    @FlowPreview
    fun asFlow(): Flow<ProductQuantity> {
        return asFlowExt()
            .map {
                val price: BigDecimal = calculateAmount(it.productUnit)
                return@map when {
                    it.maxQuantity > 0 && it.quantity > it.maxQuantity -> {
                        val amount = (price * it.maxQuantity).roundToBigDecimal()
                        it.copy(amount = amount, quantity = it.maxQuantity)
                    }
                    isQuantity -> {
                        val quantity: Double = it.quantity.round()
                        val amount = (price * it.quantity).roundToBigDecimal()
                        it.copy(amount = amount, quantity = quantity)
                    }
                    else -> {
                        val quantity: Double = it.amount.toDouble() / price.toDouble()
                        it.copy(quantity = quantity.round("#.##########"))
                    }
                }
            }
            .catch { emit(ProductQuantity(BigDecimal.ZERO, 0.0, maxQuantity)) }
    }

    fun offer() =
        offer(amount = null, quantity = null)

    fun offer(quantity: Double?) =
        offer(amount = null, quantity = quantity)

    fun offer(amount: BigDecimal) =
        offer(amount = amount, quantity = null)

    private fun offer(amount: BigDecimal? = null, quantity: Double? = null) {
        val newAmount: BigDecimal = amount ?: element.amount
        val newQuantity: Double = quantity ?: element.quantity
        element = element.copy(amount = newAmount, quantity = newQuantity)
        isQuantity = quantity != null
        offer(element)
    }

    private fun calculateAmount(productUnit: ProductUnit?): BigDecimal {
        if (productUnit == null || productUnits.isEmpty()) return amount

        var amount: BigDecimal = amount
        val actualAmount: BigDecimal = amount
        val currentIndex: Int = productUnits.indexOf(productUnit)
        val index: Int = productUnits.indexOfFirst { it.isBase }
        if (currentIndex > index) for (i: Int in index..currentIndex)
            amount = (actualAmount * productUnits[i].coefficient).roundToBigDecimal()
        else for (i: Int in index downTo currentIndex)
            amount = (actualAmount * productUnits[i].coefficient).roundToBigDecimal()

        return amount
    }

    private fun calculateProductQuantity(
        productQuantity: ProductQuantity,
        productUnit: ProductUnit
    ): ProductQuantity {
        val currentProductUnit: ProductUnit =
            productQuantity.productUnit
                ?: return productQuantity.copy(productUnit = productUnit)

        if (currentProductUnit == productUnit)
            return productQuantity.copy(productUnit = productUnit)

        val currentIndex: Int = currentProductUnit.order
        val newIndex: Int = productUnit.order
        var quantity: Double = productQuantity.quantity
        var maxQuantity: Double = productQuantity.maxQuantity

        if (currentIndex > newIndex) {
            val startIndex: Int = if (currentIndex > 0) currentIndex - 1 else 0
            for (i: Int in startIndex downTo newIndex) {
                val element: ProductUnit = productUnits[i]
                val coefficient: Double =
                    element.coefficient
                        .let { if (element.isBase) it * 10.0 else it }
                        .let { it / it }

                if (coefficient >= 1.0) {
                    maxQuantity *= coefficient * 10
                    quantity *= coefficient * 10
                } else {
                    maxQuantity /= coefficient / 10
                    quantity /= coefficient / 10
                }
            }
        } else {
            val startIndex: Int = currentIndex + 1
            for (i: Int in startIndex..newIndex) {
                val element: ProductUnit = productUnits[i]
                val coefficient: Double =
                    element.coefficient
                        .let { if (element.isBase) it * 10.0 else it }
                        .let { it / it }

                if (coefficient >= 1.0) {
                    maxQuantity /= coefficient * 10
                    quantity /= coefficient * 10
                } else {
                    maxQuantity *= coefficient / 10
                    quantity *= coefficient / 10
                }
            }
        }

        val amount: BigDecimal = productUnit.price?.multiply(quantity) ?: BigDecimal.ZERO

        return productQuantity.copy(
            amount = amount,
            quantity = if (maxQuantity == 0.0) productQuantity.quantity else quantity,
            maxQuantity = maxQuantity,
            productUnit = productUnit
        )
    }

//    private fun calculateProductQuantity(
//        productQuantity: ProductQuantity,
//        productUnit: ProductUnit
//    ): ProductQuantity {
//        val currentProductUnit: ProductUnit =
//            productQuantity.productUnit ?: return productQuantity.copy(productUnit = productUnit)
//
//        if (currentProductUnit == productUnit)
//            return productQuantity.copy(productUnit = productUnit)
//
//        val currentCoefficient: Double =
//            currentProductUnit.coefficient.let { if (it < 10.0) it * 10 else it }
//        val newCoefficient: Double = productUnit.coefficient.let { if (it < 10.0) it * 10 else it }
//
//        val index: Int = currentProductUnit.order
//        val newIndex: Int = productUnit.order
//
//        var quantity: Double = productQuantity.quantity
//        var maxQuantity: Double = productQuantity.maxQuantity
//
//        val coefficient = if (currentCoefficient == newCoefficient) 1.0 else newCoefficient
//
//        if (index > newIndex) {
//            maxQuantity = (quantity * currentCoefficient) / coefficient
//            quantity = (quantity * currentCoefficient) / coefficient
//        } else {
//            maxQuantity = (quantity * currentCoefficient) / coefficient
//            quantity = (quantity * currentCoefficient) / coefficient
//        }
//
//        val amount: BigDecimal = productUnit.price?.multiply(quantity) ?: BigDecimal.ZERO
//
//        return productQuantity.copy(
//            amount = amount,
//            quantity = if (maxQuantity == 0.0) productQuantity.quantity else quantity,
//            maxQuantity = maxQuantity,
//            productUnit = productUnit
//        )
//    }
}