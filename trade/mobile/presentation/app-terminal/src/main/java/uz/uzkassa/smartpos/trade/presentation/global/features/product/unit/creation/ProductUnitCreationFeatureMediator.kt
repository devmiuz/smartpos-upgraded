package uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureArgs
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureCallback
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.ProductUnitCreationFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.runner.ProductUnitCreationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class ProductUnitCreationFeatureMediator(
    private val router: Router
) : FeatureMediator, ProductUnitCreationFeatureArgs,
    ProductUnitCreationFeatureCallback {
    override var productUnits: List<ProductUnit> by Delegates.notNull()
    private var finishAction: ((List<ProductUnit>) -> kotlin.Unit) by Delegates.notNull()

    val featureRunner: ProductUnitCreationFeatureRunner =
        FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : ProductUnitCreationFeatureRunner {
        override fun run(units: List<ProductUnit>, action: (Screen) -> kotlin.Unit) {
            this@ProductUnitCreationFeatureMediator.productUnits = units
            router.navigateTo(Screens.ProductUnitCreationScreen)
        }

        override fun finish(action: (List<ProductUnit>) -> kotlin.Unit): ProductUnitCreationFeatureRunner {
            finishAction = action
            return this
        }
    }

    override fun onFinishProductUnitCreation(list: List<ProductUnit>) {
        finishAction.invoke(list)
    }

    private object Screens {
        object ProductUnitCreationScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                ProductUnitCreationFragment.newInstance()
        }
    }
}