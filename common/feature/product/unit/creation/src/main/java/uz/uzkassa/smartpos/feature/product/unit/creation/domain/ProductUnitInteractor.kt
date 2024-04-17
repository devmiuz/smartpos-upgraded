package uz.uzkassa.smartpos.feature.product.unit.creation.domain

import kotlinx.coroutines.channels.sendBlocking
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.feature.product.unit.creation.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper.mapToDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper.mapToResult
import uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper.mapToTopDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper.mapToTopResult
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import javax.inject.Inject
import kotlin.properties.Delegates

internal class ProductUnitInteractor @Inject constructor(
    private val productUnitBroadcastChannel: ProductUnitBroadcastChannel
) {

    private val unitDetails: MutableList<ProductUnitDetails> = arrayListOf()
    private var isTop: Boolean = true
    private var currentDetails: ProductUnitDetails by Delegates.notNull()


    fun getProductDetailsItem(details: ProductUnitDetails) {
        this.currentDetails = details
    }

    fun getUnitDetails(): MutableList<ProductUnitDetails> {
        return unitDetails
    }

    fun getProductUnits(): List<ProductUnit> {
        val baseProductUnitDetailsIndex: Int = unitDetails.indexOfFirst { it.isBase }
        return if (baseProductUnitDetailsIndex > 0) {
            val bottomProductUnits: List<ProductUnit> =
                unitDetails.subList(baseProductUnitDetailsIndex, unitDetails.size).mapToResult()

            val topProductUnits: List<ProductUnit> =
                unitDetails.subList(0, baseProductUnitDetailsIndex).mapToTopResult()

            topProductUnits + bottomProductUnits
        } else unitDetails.mapToResult()
    }

    fun setIsTop(value: Boolean) {
        isTop = value
    }

    fun calculateProductUnitDetails(list: List<ProductUnit>): List<ProductUnitDetails> {
        val baseProductUnit: ProductUnit = list.first { it.isBase }
        val baseProductUnitIndex: Int = list.indexOfFirst { it.isBase }
        unitDetails.clear()

        if (baseProductUnitIndex > 0) {
            val bottomProductUnitDetails: List<ProductUnitDetails> =
                list.subList(baseProductUnitIndex, list.size).mapToDetails(baseProductUnit)

            val topProductUnitDetails: List<ProductUnitDetails> =
                list.subList(0, baseProductUnitIndex).mapToTopDetails(baseProductUnit)

            unitDetails.addAll(topProductUnitDetails + bottomProductUnitDetails)

        } else unitDetails.addAll(list.mapToDetails(baseProductUnit))
        return unitDetails
    }

    fun add(index: Int, details: ProductUnitDetails) {
        unitDetails.add(index, details)
        productUnitBroadcastChannel.sendBlocking(getProductUnitDetails())
    }

    private fun getProductUnitDetails(): List<ProductUnitDetails> {
        val parentIndex: Int = unitDetails.indexOfFirst { it.isBase }
        val actualIndex: Int = unitDetails.indexOf(currentDetails)
        if (parentIndex > actualIndex) {
            when {
                actualIndex == 1 && isTop -> {
                    updateTopCoefficient(list = unitDetails, currentIndex = actualIndex)
                    updateTopProductUnit(actualIndex - 1)
                    updateOrder(unitDetails, actualIndex)
                }

                actualIndex in 2 until parentIndex && isTop -> {
                    updateTopCoefficient(list = unitDetails, currentIndex = actualIndex)
                    updateTopProductUnit(actualIndex - 1)
                    replaceParentUnit(actualIndex - 2)
                    updateOrder(unitDetails, actualIndex - 1)
                }

                else -> {
                    updateTopCoefficient(list = unitDetails, currentIndex = actualIndex)
                    updateTopProductUnit(actualIndex + 1)
                    replaceParentUnit(actualIndex)
                    updateOrder(unitDetails, actualIndex)
                }
            }
        } else {
            when {
                unitDetails[actualIndex].isBase && isTop -> {

                    if (parentIndex > 1) {
                        updateTopCoefficient(list = unitDetails, currentIndex = actualIndex )
                        updateTopProductUnit(actualIndex - 1)
                        replaceParentUnit(actualIndex - 2)
                        updateOrder(unitDetails, actualIndex - 1)

                    } else {
                        updateTopCoefficient(unitDetails, actualIndex)
                        updateTopProductUnit(actualIndex - 1)
                        updateOrder(unitDetails, actualIndex)
                    }
                }

                unitDetails[actualIndex].isBase && !isTop && unitDetails.size == actualIndex + 2 -> {
                    updateBottomCoefficient(unitDetails, actualIndex + 1)
                    updateOrder(unitDetails, actualIndex + 1)
                }
                unitDetails[actualIndex].isBase && unitDetails.size > actualIndex + 2 && !isTop -> {
                    updateBottomCoefficient(unitDetails, actualIndex + 1)
                    updateBottomProductUnit(actualIndex + 2)
                    updateOrder(unitDetails, actualIndex)
                }

                unitDetails.size == actualIndex + 1 && !isTop -> {
                    updateBottomBaseCoefficient(unitDetails, actualIndex + 1)
                    updateOrder(unitDetails, actualIndex + 1)
                }

                else -> {
                    if (isTop) {
                        updateBottomCoefficient(list = unitDetails, currentIndex = actualIndex - 1)
                        updateOrder(unitDetails, actualIndex - 1)
                    } else if (unitDetails.size == actualIndex + 2 && !isTop) {
                        updateBottomCoefficient(list = unitDetails, currentIndex = actualIndex)
                        updateOrder(unitDetails, actualIndex + 1)
                    } else {
                        updateBottomCoefficient(list = unitDetails, currentIndex = actualIndex + 1)
                        updateBottomProductUnit(actualIndex + 2)
                        updateOrder(unitDetails, actualIndex)
                    }
                }
            }
        }
        return unitDetails
    }

    private fun replaceParentUnit(index: Int) {
        unitDetails[index] =
            unitDetails[index].copy(unit = checkNotNull(unitDetails[index + 1].parentUnit))
    }

    private fun updateTopProductUnit(index: Int) {
        unitDetails[index] = unitDetails[index].copy(
            parentUnit = unitDetails[index].unit,
            unit = checkNotNull(unitDetails[index].parentUnit)
        )
    }

    private fun updateBottomCoefficient(list: MutableList<ProductUnitDetails>, currentIndex: Int) {
        return list.forEachIndexed { index, _ ->
            if (index >= currentIndex)
                list[index] =
                    list[index].copy(coefficient = list[index].count * list[index - 1].coefficient)
        }
    }

    private fun updateBottomBaseCoefficient(
        list: MutableList<ProductUnitDetails>,
        currentIndex: Int
    ) {
        return list.forEachIndexed { index, _ ->
            if (index >= currentIndex)
                list[index] =
                    list[index].copy(coefficient = list[index].coefficient * list[index - 1].count)
        }
    }

    private fun updateTopCoefficient(list: MutableList<ProductUnitDetails>, currentIndex: Int) {
        for (i: Int in currentIndex - 1 downTo 0) {
            list[i] =
                list[i].copy(coefficient = list[i + 1].coefficient / list[i].count)
        }
    }

    private fun updateBottomProductUnit(index: Int) {
        unitDetails[index] =
            unitDetails[index].copy(
                parentUnit = unitDetails[index - 1].unit,
                coefficient = unitDetails[index].count * unitDetails[index - 1].coefficient
            )
    }

    private fun updateOrder(list: MutableList<ProductUnitDetails>, currentIndex: Int) {
        return list.forEachIndexed { index, _ ->
            if (index >= currentIndex)
                list[index] = list[index].copy(order = index)
        }
    }
}