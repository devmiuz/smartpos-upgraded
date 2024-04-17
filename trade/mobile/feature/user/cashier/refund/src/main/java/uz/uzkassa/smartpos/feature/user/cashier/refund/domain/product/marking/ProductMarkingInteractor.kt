package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.product.marking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.product.marking.ProductMarkingRepository
import javax.inject.Inject

internal class ProductMarkingInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productMarkingRepository: ProductMarkingRepository
) {

    fun deleteProductMarkings(productMarkings: List<ProductMarking>): Flow<Result<Unit>> {
        return flow {
            productMarkings.forEach {
                productMarkingRepository.deleteMarking(it)
            }
            emit(Result.success(Unit))
        }
            .flowOn(coroutineContextManager.ioContext)
    }
}