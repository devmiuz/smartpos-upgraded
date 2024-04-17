package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking

import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.marking.ProductMarkingRepository
import javax.inject.Inject

internal class ProductMarkingInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productMarkingRepository: ProductMarkingRepository
) {

    fun deleteLastItems(markings: Array<String>, productId: Long?, count: Int): Flow<Result<Unit>> {
        return productMarkingRepository
            .getProductMarkings(productId)
            .onEach { productMarkings ->
                if (productMarkings.isNotEmpty()) {
                    if (count <= markings.size) {
                        val removableMarkings = markings.takeLast(count)
                        removableMarkings.forEach {
                            val list = productMarkings.filter { mark -> mark.marking == it }
                            if (list.isNotEmpty()) productMarkingRepository.deleteMarking(list.first())
                        }
                    }
                }
            }
            .map { }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun saveProductMarkings(productMarkings: List<ProductMarking>): Flow<Result<Unit>> {
        return flow {
            productMarkingRepository.updateOrInsertProductMarkings(productMarkings)
            emit(Result.success(Unit))
        }
            .flowOn(coroutineContextManager.ioContext)
    }

    fun deleteProductMarkings(productMarkings: List<ProductMarking>): Flow<Result<Unit>> {
        return flow {
            productMarkings.forEach {
                productMarkingRepository.deleteMarkingByProductId(it)
            }
            emit(Result.success(Unit))
        }
            .flowOn(coroutineContextManager.ioContext)
    }
}