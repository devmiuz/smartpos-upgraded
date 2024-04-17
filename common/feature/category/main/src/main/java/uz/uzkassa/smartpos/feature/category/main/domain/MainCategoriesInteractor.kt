package uz.uzkassa.smartpos.feature.category.main.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.category.common.domain.CategoryCommonInteractor
import uz.uzkassa.smartpos.feature.category.main.data.channel.HasEnabledCategoriesBroadcastChannel
import uz.uzkassa.smartpos.feature.category.main.data.repository.MainCategoriesRepository
import uz.uzkassa.smartpos.feature.category.main.data.repository.params.EnabledCategoriesParams
import javax.inject.Inject

internal class MainCategoriesInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val hasEnabledCategoriesBroadcastChannel: HasEnabledCategoriesBroadcastChannel,
    private val mainCategoriesRepository: MainCategoriesRepository
) : CategoryCommonInteractor() {

    fun getCategories(): Flow<Result<List<Category>>> =
        mainCategoriesRepository
            .getMainCategories()
            .onEach { setCategories(it) }
            .onEach { hasEnabledCategoriesBroadcastChannel.send(hasAtLeastOneEnabledCategory()) }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)

    @FlowPreview
    fun setEnabledCategories(): Flow<Result<Unit>> {
        return flowOf(getListCategories())
            .flatMapConcat {
                return@flatMapConcat if (it.isEmpty()) flowOf(Unit)
                else mainCategoriesRepository.setEnabledCategories(EnabledCategoriesParams(it))
                    .map { Unit }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun selectCategory(category: Category) {
        upsertCategory(category, false)
        hasEnabledCategoriesBroadcastChannel.sendBlocking(hasAtLeastOneEnabledCategory())
    }
}