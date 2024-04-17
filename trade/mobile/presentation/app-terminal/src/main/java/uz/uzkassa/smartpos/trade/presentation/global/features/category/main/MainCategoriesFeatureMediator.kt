package uz.uzkassa.smartpos.trade.presentation.global.features.category.main

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureArgs
import uz.uzkassa.smartpos.feature.category.main.dependencies.MainCategoriesFeatureCallback
import uz.uzkassa.smartpos.feature.category.main.presentation.MainCategoriesFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.main.runner.MainCategoriesFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class MainCategoriesFeatureMediator : FeatureMediator, MainCategoriesFeatureArgs,
    MainCategoriesFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()

    val featureRunner: MainCategoriesFeatureRunner =
        FeatureRunnerImpl()

    override fun onBackFromMainCategories() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : MainCategoriesFeatureRunner {

        override fun run(branchId: Long, action: (Screen) -> Unit) {
            this@MainCategoriesFeatureMediator.branchId = branchId
            action.invoke(Screens.MainCategoriesScreen)
        }

        override fun back(action: () -> Unit): MainCategoriesFeatureRunner {
            backAction = action
            return this
        }

    }

    private object Screens {

        object MainCategoriesScreen : SupportAppScreen() {
            override fun getFragment(): Fragment =
                MainCategoriesFragment.newInstance()
        }
    }
}