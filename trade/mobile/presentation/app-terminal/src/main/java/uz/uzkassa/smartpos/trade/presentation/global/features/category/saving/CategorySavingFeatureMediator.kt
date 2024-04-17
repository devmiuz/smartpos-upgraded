package uz.uzkassa.smartpos.trade.presentation.global.features.category.saving

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureArgs
import uz.uzkassa.smartpos.feature.category.saving.dependencies.CategorySavingFeatureCallback
import uz.uzkassa.smartpos.feature.category.saving.presentation.CategoryCreationFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.saving.runner.CategorySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CategorySavingFeatureMediator : FeatureMediator,
    CategorySavingFeatureArgs, CategorySavingFeatureCallback {
    private var finishAction: ((Category) -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override var categoryParentId: Long? = null

    val featureRunner: CategorySavingFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishCategorySaving(category: Category) =
        finishAction.invoke(category)

    private inner class FeatureRunnerImpl : CategorySavingFeatureRunner {

        override fun run(branchId: Long, categoryParentId: Long?, action: (Screen) -> Unit) {
            this@CategorySavingFeatureMediator.branchId = branchId
            this@CategorySavingFeatureMediator.categoryParentId = categoryParentId
            action.invoke(Screens.CategoryCreationScreen)
        }

        override fun finish(action: (Category) -> Unit): CategorySavingFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object CategoryCreationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CategoryCreationFragment.newInstance()
        }
    }
}