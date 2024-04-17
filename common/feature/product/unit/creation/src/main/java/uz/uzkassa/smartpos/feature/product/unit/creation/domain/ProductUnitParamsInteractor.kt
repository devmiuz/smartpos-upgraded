package uz.uzkassa.smartpos.feature.product.unit.creation.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.text.TextUtils
import uz.uzkassa.smartpos.feature.product.unit.creation.data.exception.UnitCreationException
import uz.uzkassa.smartpos.feature.product.unit.creation.data.mapper.mapToDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitParams
import uz.uzkassa.smartpos.feature.product.unit.creation.data.repository.UnitRepository
import javax.inject.Inject
import kotlin.properties.Delegates

internal class ProductUnitParamsInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val unitRepository: UnitRepository,
    private val productUnitCreationInteractor: ProductUnitCreationInteractor,
    private val productUnitInteractor: ProductUnitInteractor
) {
    private var count: Double? = null
    private var currentUnit: Unit? = null
    private var index: Int by Delegates.notNull()
    private var isTop: Boolean = true
    private var parentUnit: Unit? = null
    private var unitId: Long? = null
    private var coefficient: Double? = null

    fun getCurrentUnit(): Unit {
        return checkNotNull(currentUnit)
    }

    fun setParentUnit(unit: Unit) {
        parentUnit = unit
    }

    fun setProductUnitCount(value: String) {
        this.count = TextUtils.replaceAllLetters(value).toDoubleOrNull()
    }

    fun setUnitId(unitId: Long) {
        this.unitId = unitId
    }

    fun setCoefficient(coefficient: Double) {
        this.coefficient = coefficient
    }

    fun setUnitIndex(index: Int) {
        this.index = index
    }

    fun setIsTop(value: Boolean) {
        isTop = value
    }

    fun sendResult(): Flow<Result<kotlin.Unit>> {
        val exception = UnitCreationException(
            isUnitCountNotDefined = count == null,
            isUnitNotDefined = parentUnit == null
        )
        return when {
            exception.isPassed -> flowOf(Result.failure(exception))
            else -> addProductUnits()
        }
    }

    @FlowPreview
    fun getUnits(): Flow<Result<List<Unit>>> {
        return unitRepository.getUnits()
            .map {
                setCurrentUnit(it)
                productUnitCreationInteractor.removeUnits(it)
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun addProductUnits(): Flow<Result<kotlin.Unit>> {

        val details: ProductUnitDetails =
            ProductUnitParams(
                currentUnit = checkNotNull(currentUnit),
                count = checkNotNull(count),
                coefficient = checkNotNull(coefficient),
                parentUnit = checkNotNull(parentUnit),
                order = 0
            ).mapToDetails()

        if (isTop)
            productUnitInteractor.add(index, details)
        else
            productUnitInteractor.add(index + 1, details)
        return flowOf(Unit)
            .flatMapResult()
            .flowOn(coroutineContextManager.defaultContext)

    }

    private fun setCurrentUnit(list: List<Unit>) {
        this.currentUnit = list.first { it.id == unitId }
    }
}