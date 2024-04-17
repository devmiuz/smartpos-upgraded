package uz.uzkassa.smartpos.trade.presentation.global.features.category.type

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureArgs
import uz.uzkassa.smartpos.feature.category.type.dependencies.CategoryTypeFeatureCallback
import uz.uzkassa.smartpos.feature.category.type.presentation.CategoryTypeFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.list.runner.CategoryListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.runner.MainCategoriesFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.category.type.runner.CategoryTypeFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CategoryTypeFeatureMediator(
    private val categoryListFeatureRunner: CategoryListFeatureRunner,
    private val mainCategoriesFeatureRunner: MainCategoriesFeatureRunner,
    private val router: Router
) : FeatureMediator, CategoryTypeFeatureArgs, CategoryTypeFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var branchId: Long by Delegates.notNull()

    val featureRunner: CategoryTypeFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenCategoryList() {
        categoryListFeatureRunner
            .back { router.backTo(Screens.CategoryTypeScreen) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onOpenMainCategories() {
        mainCategoriesFeatureRunner
            .back { router.backTo(Screens.CategoryTypeScreen) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onBackFromCategoryType() =
        router.exit()

    private inner class FeatureRunnerImpl : CategoryTypeFeatureRunner {

        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@CategoryTypeFeatureMediator.branchId = branchId
            action.invoke(Screens.CategoryTypeScreen)
        }

        override fun back(action: () -> Unit): CategoryTypeFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object CategoryTypeScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                CategoryTypeFragment.newInstance()
        }
    }
}