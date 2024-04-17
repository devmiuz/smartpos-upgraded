package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.selection

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.product.ProductListResource.Success.Type.*
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import javax.inject.Inject

internal class ProductSelectionInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val productRepository: ProductRepository
) {
    private val broadcastChannel: BroadcastChannelWrapper<Action> = BroadcastChannelWrapper()
    private var action: Action? = null
    private var pagination: Action.Pagination? = null

    fun changeProductFavoriteState(product: Product): Flow<Result<Product>> {
        val isFavorite: Boolean = !product.isFavorite
        return productRepository
            .let {
                return@let if (isFavorite) it.addToFavoritesByProductId(product.id)
                else it.deleteFromFavoritesByProductId(product.id)
            }
            .map { product.copy(isFavorite = isFavorite) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun findProduct(query: String) {
        broadcastChannel.sendBlocking(Action.Search(query))
    }

    fun setPage(page: Int) {
        broadcastChannel.sendBlocking(Action.Pagination(page))
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getProducts(): Flow<ProductListResource> {
        return broadcastChannel
            .asFlow()
            .onStart { emit(Action.Favorite) }
            .onEach {
                if (it is Action.Pagination) pagination = it
                else {
                    val isSearchQueryChanged: Boolean =
                        (action as? Action.Search)?.query != (it as? Action.Search)?.query
                    if (it.javaClass != action?.javaClass || isSearchQueryChanged) {
                        pagination = Action.Pagination(0)
                    }

                    action = it
                }
            }
            .filter { action != null && pagination != null }
            .flatMapMerge {
                val page: Int = checkNotNull(pagination).page
                return@flatMapMerge flowOf(checkNotNull(action))
                    .filterNotNull()
                    .flatMapConcat { action ->
                        return@flatMapConcat when (action) {
                            is Action.Search -> when {
                                action.query.isNotEmpty() ->
                                    productRepository
                                        .findProductsByName(action.query, page)
                                        .map { ProductListResource.Success(SEARCH, it) }
                                else -> productRepository
                                    .getFavoriteProducts(0)
                                    .onEach {
                                        this.action = Action.Favorite
                                        pagination = Action.Pagination(0)
                                    }
                                    .map { ProductListResource.Success(FAVORITE, it) }
                            }
                            else -> productRepository
                                .getFavoriteProducts(page)
                                .map { ProductListResource.Success(FAVORITE, it) }
                        }.map { it as ProductListResource }
                            .catch { emit(ProductListResource.Failure(it)) }
                            .onStart { emit(ProductListResource.Loading) }
                    }
                    .flowOn(coroutineContextManager.ioContext)
            }
    }

    private sealed class Action {
        object Favorite : Action()
        data class Pagination(val page: Int) : Action()
        data class Search(val query: String) : Action()
    }
}