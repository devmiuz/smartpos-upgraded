package uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureCallback
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.ProductQuantityFragment
import uz.uzkassa.smartpos.trade.companion.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.companion.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class ProductQuantityFeatureMediator : FeatureMediator, ProductQuantityFeatureArgs,
    ProductQuantityFeatureCallback {
    private var finishAction: ((ProductQuantityResult) -> Unit) by Delegates.notNull()

    override var amount: BigDecimal by Delegates.notNull()
    override var productId: Long by Delegates.notNull()
    override var productName: String by Delegates.notNull()
    override var quantity: Double by Delegates.notNull()
    override var maxQuantity: Double by Delegates.notNull()
    override var unitId: Long? = null

    val featureRunner: ProductQuantityFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishProductQuantity(productQuantityResult: ProductQuantityResult) =
        finishAction.invoke(productQuantityResult)

    private inner class FeatureRunnerImpl : ProductQuantityFeatureRunner {
        override fun run(
            amount: BigDecimal,
            maxQuantity: Double,
            productId: Long,
            productName: String,
            quantity: Double,
            unitId: Long?,
            action: (Screen) -> Unit
        ) {
            this@ProductQuantityFeatureMediator.amount = amount
            this@ProductQuantityFeatureMediator.productId = productId
            this@ProductQuantityFeatureMediator.productName = productName
            this@ProductQuantityFeatureMediator.quantity = quantity
            this@ProductQuantityFeatureMediator.maxQuantity = maxQuantity
            this@ProductQuantityFeatureMediator.unitId = unitId
            action.invoke(Screens.HelperQuantity)
        }

        override fun finish(action: (ProductQuantityResult) -> Unit): ProductQuantityFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object HelperQuantity : SupportAppScreen() {
            override fun getFragment(): Fragment =
                ProductQuantityFragment.newInstance()
        }
    }
}