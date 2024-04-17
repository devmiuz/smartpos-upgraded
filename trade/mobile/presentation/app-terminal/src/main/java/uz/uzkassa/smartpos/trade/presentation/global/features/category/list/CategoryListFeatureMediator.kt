package uz.uzkassa.smartpos.trade.presentation.global.features.category.list

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureArgs
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureCallback
import uz.uzkassa.smartpos.feature.category.list.presentation.CategoryListFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.runner.CategoryListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.runner.CategorySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.runner.ProductListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CategoryListFeatureMediator(
    private val categorySavingFeatureRunner: CategorySavingFeatureRunner,
    private val productListFeatureRunner: ProductListFeatureRunner,
    private val router: Router
) : FeatureMediator, CategoryListFeatureArgs, CategoryListFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var categoryParentId: Long? = null
    override val categoryBroadcastChannel = BroadcastChannelWrapper<Category>()

    val featureRunner: CategoryListFeatureRunner =
        FeatureRunnerImpl()

    override fun onCreateCategory(categoryParentId: Long?) {
        categorySavingFeatureRunner
            .finish { categoryBroadcastChannel.sendBlocking(it) }
            .run(branchId, categoryParentId) { router.navigateTo(it) }
    }

    override fun onCategorySelected(categoryId: Long, categoryName: String) =
        productListFeatureRunner
            .back { router.backTo(Screens.CategoryListScreen) }
            .run(branchId, categoryId, categoryName) { router.navigateTo(it) }


    override fun onBackFromCategoryList() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : CategoryListFeatureRunner {

        override fun run(branchId: Long, categoryParentId: Long?, action: (Screen) -> Unit) {
            this@CategoryListFeatureMediator.branchId = branchId
            this@CategoryListFeatureMediator.categoryParentId = categoryParentId
            action.invoke(Screens.CategoryListScreen)
        }

        override fun back(action: () -> Unit): CategoryListFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object CategoryListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CategoryListFragment.newInstance()
        }
    }
}