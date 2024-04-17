package uz.uzkassa.smartpos.trade.presentation.global.features.category.setup

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureArgs
import uz.uzkassa.smartpos.feature.category.selection.setup.dependencies.CategorySetupFeatureCallback
import uz.uzkassa.smartpos.feature.category.selection.setup.presentation.CategorySetupFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.setup.runner.CategorySetupFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class CategorySetupFeatureMediator : FeatureMediator, CategorySetupFeatureArgs,
    CategorySetupFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var finishAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()

    val featureRunner: CategorySetupFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromCategorySetup() =
        backAction.invoke()

    override fun onFinishCategorySetup() =
        finishAction.invoke()

    private inner class FeatureRunnerImpl : CategorySetupFeatureRunner {

        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@CategorySetupFeatureMediator.branchId = branchId
            action.invoke(Screens.CategorySetupScreen)
        }

        override fun back(action: () -> Unit): CategorySetupFeatureRunner {
            this@CategorySetupFeatureMediator.backAction = action
            return this
        }

        override fun finish(action: () -> Unit): CategorySetupFeatureRunner {
            this@CategorySetupFeatureMediator.finishAction = action
            return this
        }
    }

    private object Screens {

        object CategorySetupScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                CategorySetupFragment.newInstance()
        }
    }
}