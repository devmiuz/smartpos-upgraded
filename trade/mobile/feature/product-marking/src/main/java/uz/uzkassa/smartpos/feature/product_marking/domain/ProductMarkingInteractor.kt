package uz.uzkassa.smartpos.feature.product_marking.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.product_marking.data.exception.DuplicateProductMarkingException
import uz.uzkassa.smartpos.feature.product_marking.data.exception.NotFoundProductMarkingException
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.product_marking.data.repository.ProductMarkingRepository
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureArgs
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit as ProductUnit

internal class ProductMarkingInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productMarkingRepository: ProductMarkingRepository,
    private val productMarkingFeatureArgs: ProductMarkingFeatureArgs
) {

    private var forRefund: Boolean = productMarkingFeatureArgs.forRefund
    private var uid: Long? = productMarkingFeatureArgs.uid
    private val categoryId: Long? = productMarkingFeatureArgs.categoryId
    private val categoryName: String? = productMarkingFeatureArgs.categoryName
    private var productId: Long? = productMarkingFeatureArgs.productId
    private var lastUnitId: Long? = productMarkingFeatureArgs.lastUnitId
    private var amount: BigDecimal = productMarkingFeatureArgs.amount
    private var price: BigDecimal = productMarkingFeatureArgs.price
    private val barcode: String? = productMarkingFeatureArgs.barcode
    private var productPrice: BigDecimal = productMarkingFeatureArgs.productPrice
    private var vatRate: BigDecimal? = productMarkingFeatureArgs.vatRate
    private var productName: String = productMarkingFeatureArgs.productName
    private var unit: ProductUnit? = productMarkingFeatureArgs.unit
    private var realQuantity: Double = floor(productMarkingFeatureArgs.quantity)
    private var quantity: Double = abs(realQuantity)

    private val totalMarkings = productMarkingFeatureArgs.totalMarkings
    private val initialMarkings = productMarkingFeatureArgs.initialMarkings
    private val scannedMarkings = productMarkingFeatureArgs.scannedMarkings

    private var markedMarkings: MutableList<String> = mutableListOf()

    fun getTotalProductQuantity(): Int = ceil(quantity).toInt()

    fun getMarkedProductQuantity() = markedMarkings.size + scannedMarkings.size

    fun getMarkedMarkings(): Array<String> = markedMarkings.toTypedArray()

    fun getProductMarkingResult(): ProductMarkingResult = ProductMarkingResult(
        uid = uid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        lastUnitId = lastUnitId,
        amount = amount,
        productPrice = productPrice,
        barcode = barcode,
        productName = productName,
        price = price,
        vatRate = vatRate,
        quantity = realQuantity,
        unit = unit,
        markings = (markedMarkings + scannedMarkings).toTypedArray()
    )

    private fun getRealMarking(rawMarking: String): String {
        var marking = rawMarking
        println("initialLength ${marking.length}")
        if (rawMarking.length == 29) {
            marking = rawMarking.substring(0, 21)
        } else if (rawMarking.length > 29) {
            val startIndex1 = 2

            var gtinStartIndex = rawMarking.indexOf("21")
            println("initial lastIndex1 $gtinStartIndex")
            if (gtinStartIndex == -1) return marking
            val gtinMaxLength = 16
            gtinStartIndex = if (gtinStartIndex - gtinMaxLength > 0) gtinMaxLength else gtinStartIndex
            println("actual lastIndex1 $gtinStartIndex")
            marking = rawMarking.substring(startIndex1, gtinStartIndex)
            println("firstMarking $marking")

            val startIndex2 = rawMarking.indexOf("21") + 2
            println("startIndex2 $startIndex2")

            var unprintableSignIndex = rawMarking.indexOf("\u001D")
            println("initial lastIndex2 $unprintableSignIndex")
            unprintableSignIndex =
                if (unprintableSignIndex == -1 || unprintableSignIndex < startIndex2) {
                    val ciiIndex = checkCIICode(rawMarking, startIndex2)

                    val serialNumberMaxLength = 22

                    if (ciiIndex != -1) {
                        ciiIndex
                    } else {
                        val maxLastIndex = startIndex2 + serialNumberMaxLength
                        if (rawMarking.length > maxLastIndex) maxLastIndex else rawMarking.length
                    }

                } else {
                    unprintableSignIndex
                }
            println("actual lastIndex2 $unprintableSignIndex")

            println("secondMarking ${rawMarking.substring(startIndex2, unprintableSignIndex)}")
            marking += rawMarking.substring(startIndex2, unprintableSignIndex)
        }
        println("result marking $marking")
        println("markingResultLength ${marking.length}")
        return marking
    }

    fun checkCIICode(rawMarking: String, startIndex: Int): Int {
        println("----------->")
        var codeFoundIndex = -1
        for (code in (91..99)) {
            println("code : $code")
            val index = rawMarking.indexOf(code.toString(), startIndex, true)
            println("index : $index")
            if (index != -1) {
                codeFoundIndex = index
                break
            }
        }
        println("<------------")
        return codeFoundIndex
    }

    @FlowPreview
    fun setMarkingForProduct(rawMarking: String): Flow<Result<Unit>> {
        val marking = getRealMarking(rawMarking)
        return productMarkingRepository
            .getAllProductMarkings()
            .flatMapConcat {
                if (forRefund) {
                    val isValid: Boolean =
                        totalMarkings!!.contains(marking)
                                && initialMarkings.contains(marking).not()
                                && markedMarkings.contains(marking).not()
                    if (isValid) {
                        markedMarkings.add(marking)
                        flowOf(Result.success(Unit))
                    } else {
                        flowOf(Result.failure(NotFoundProductMarkingException()))
                    }
                } else {
                    val isValid: Boolean =
                        it.map { mark -> mark.marking }
                            .contains(marking).not()
                                && if (realQuantity < 0)
                            totalMarkings.contains(marking)
                        else
                            totalMarkings.contains(marking).not()
                                    && markedMarkings.contains(marking).not()
                                    && initialMarkings.contains(marking).not()
                    if (isValid) {
                        markedMarkings.add(marking)
                        flowOf(Result.success(Unit))
                    } else {
                        flowOf(Result.failure(DuplicateProductMarkingException()))
                    }
                }
            }
            .flowOn(coroutineContextManager.ioContext)
    }
}