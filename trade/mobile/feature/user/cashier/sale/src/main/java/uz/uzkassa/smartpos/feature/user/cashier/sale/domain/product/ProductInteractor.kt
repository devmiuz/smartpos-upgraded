package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.product.ProductNotFoundException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import javax.inject.Inject

internal class ProductInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productRepository: ProductRepository
) {

    fun getProductByProductId(productId: Long): Flow<Product> {
        return productRepository
            .getProductByProductId(productId)
            .map { it ?: throw ProductNotFoundException() }
            .flowOn(coroutineContextManager.ioContext)
    }

    @FlowPreview
    fun getProductByBarcode(barcode: String): Flow<Result<Product>> {
        return productRepository
            .getProductByBarcode(readBarcodeFromMarking(barcode))
            .flatMapResult()
            .flatMapConcat { it ->
                it.getOrNull()?.let { flowOf(Result.success(it)) }
                    ?: flowOf(Result.failure(ProductNotFoundException()))
            }
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun readBarcodeFromMarking(rawMarking: String): String {
        var marking = rawMarking
        println("initialLength ${marking.length}")
        if (rawMarking.length == 29) {
            marking = rawMarking.substring(0, 21)

            var gtinStartIndex = rawMarking.indexOf("21")
            println("initial lastIndex1 $gtinStartIndex")
            if (gtinStartIndex == -1) return marking
            val gtinMaxLength = 16
            gtinStartIndex =
                if (gtinStartIndex - gtinMaxLength > 0) gtinMaxLength else gtinStartIndex
            println("actual lastIndex1 $gtinStartIndex")

            marking = rawMarking.substring(rawMarking.indexOf("010") + 3, gtinStartIndex + 1)

        } else if (rawMarking.length > 29) {
            val startIndex1 = 2

            var gtinStartIndex = rawMarking.indexOf("21")
            println("initial lastIndex1 $gtinStartIndex")
            if (gtinStartIndex == -1) return marking
            val gtinMaxLength = 16
            gtinStartIndex =
                if (gtinStartIndex - gtinMaxLength > 0) gtinMaxLength else gtinStartIndex
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

            marking = rawMarking.substring(rawMarking.indexOf("010") + 3, gtinStartIndex + 1)
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
}