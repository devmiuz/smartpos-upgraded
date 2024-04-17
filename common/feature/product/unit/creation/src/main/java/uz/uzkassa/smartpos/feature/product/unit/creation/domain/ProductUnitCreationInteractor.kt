package uz.uzkassa.smartpos.feature.product.unit.creation.domain

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.feature.product.unit.creation.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureArgs
import javax.inject.Inject

internal class ProductUnitCreationInteractor @Inject constructor(
    productUnitBroadcastChannel: ProductUnitBroadcastChannel,
    productUnitCreationFeatureArgs: ProductUnitCreationFeatureArgs,
    private val productUnitInteractor: ProductUnitInteractor
) {

    init {
        val list: List<ProductUnit> = productUnitCreationFeatureArgs.productUnits
        productUnitBroadcastChannel.sendBlocking(
            productUnitInteractor.calculateProductUnitDetails(list)
        )
    }

    fun setUnitDetails(): List<ProductUnitDetails> {
        return productUnitInteractor.getUnitDetails()
    }

    fun removeUnits(list: List<Unit>): List<Unit> {
        val unitsSet: HashSet<Unit> = hashSetOf()
        with(productUnitInteractor.getUnitDetails()) {
            unitsSet.addAll(this.map { it.unit } + this.mapNotNull { it.parentUnit })
        }
        val units = list.toMutableList()
        units.removeAll(unitsSet)
        return units
    }

    fun removeProductUnit(value: ProductUnitDetails): List<ProductUnitDetails> {
        val unitDetails = productUnitInteractor.getUnitDetails()
        val baseIndex: Int = unitDetails.indexOf(unitDetails.first { it.isBase })

        when (val deletableIndex: Int = unitDetails.indexOf(value)) {
            unitDetails.size - 1 -> unitDetails.removeAt(deletableIndex)
            else -> {
                if (baseIndex - deletableIndex == 1 && baseIndex > 1) {
                    unitDetails[deletableIndex - 1] =
                        unitDetails[deletableIndex - 1].copy(
                            unit = unitDetails[deletableIndex].unit
                        )
                    unitDetails.removeAt(deletableIndex)
                    updateTopCoefficient(unitDetails, deletableIndex)
                } else if (baseIndex - deletableIndex == 1 && baseIndex == 1) {
                    unitDetails.removeAt(deletableIndex)
                } else {
                    unitDetails[deletableIndex + 1] =
                        unitDetails[deletableIndex + 1].copy(
                            parentUnit = unitDetails[deletableIndex].parentUnit
                        )
                    unitDetails.removeAt(deletableIndex)
                    updateBottomCoefficient(unitDetails, deletableIndex)
                }
                unitDetails.forEachIndexed { index, _ ->
                    unitDetails[index] = unitDetails[index].copy(order = index)
                }
            }
        }
        return unitDetails
    }

    private fun updateTopCoefficient(list: MutableList<ProductUnitDetails>, currentIndex: Int) {
        for (i: Int in currentIndex - 1 downTo 0) {
            list[i] =
                list[i].copy(coefficient = list[i + 1].coefficient / list[i].count)
        }
    }

    private fun updateBottomCoefficient(list: MutableList<ProductUnitDetails>, currentIndex: Int) {
        for (i: Int in currentIndex until list.size) {
            list[i] =
                list[i].copy(coefficient = list[i - 1].coefficient * list[i].count)
        }
    }
}
