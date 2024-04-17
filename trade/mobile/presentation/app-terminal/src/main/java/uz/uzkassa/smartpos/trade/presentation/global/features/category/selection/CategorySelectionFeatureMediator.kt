package uz.uzkassa.smartpos.trade.presentation.global.features.category.selection

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureArgs
import uz.uzkassa.common.feature.category.selection.dependencies.CategorySelectionFeatureCallback
import uz.uzkassa.common.feature.category.selection.presentation.CategorySelectionFragment
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.runner.CategorySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CategorySelectionFeatureMediator(
    private val router: Router
) : FeatureMediator, CategorySelectionFeatureArgs,
    CategorySelectionFeatureCallback {
    override var branchId: Long by Delegates.notNull()
    private var finishAction: ((Category) -> Unit) by Delegates.notNull()

    val featureRunner: CategorySelectionFeatureRunner =
        FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : CategorySelectionFeatureRunner {
        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@CategorySelectionFeatureMediator.branchId = branchId
            router.navigateTo(Screens.CategorySelectionScreen)
        }

        override fun finish(action: (Category) -> Unit): CategorySelectionFeatureRunner {
            finishAction = action
            return this
        }
    }

    override fun onFinishCategorySelection(category: Category) {
        finishAction.invoke(category)
    }

    private object Screens {
        object CategorySelectionScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CategorySelectionFragment.newInstance()
        }
    }
}