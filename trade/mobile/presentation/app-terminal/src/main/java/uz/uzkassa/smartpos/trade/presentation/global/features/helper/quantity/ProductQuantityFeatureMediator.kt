package uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantityResult
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureArgs
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureCallback
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.ProductQuantityFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.helper.quantity.runner.ProductQuantityFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class ProductQuantityFeatureMediator : FeatureMediator, ProductQuantityFeatureArgs,
    ProductQuantityFeatureCallback {

    private var finishAction: ((ProductQuantityResult?) -> Unit) by Delegates.notNull()

    override var uid: Long? = null
    override var categoryId: Long? = null
    override var categoryName: String? = null
    override var productId: Long? = null
    override var unitId: Long? = null
    override var amount: BigDecimal by Delegates.notNull()
    override var price: BigDecimal by Delegates.notNull()
    override var productPrice: BigDecimal by Delegates.notNull()
    override var vatRate: BigDecimal? = null
    override var quantity: Double by Delegates.notNull()
    override var maxQuantity: Double by Delegates.notNull()
    override var barcode: String? = null
    override var productName: String by Delegates.notNull()
    override var isRefund: Boolean = false

    val featureRunner: ProductQuantityFeatureRunner =
        FeatureRunnerImpl()

    override fun onFinishProductQuantity(productQuantityResult: ProductQuantityResult?) {
        finishAction.invoke(productQuantityResult)
    }

    private inner class FeatureRunnerImpl : ProductQuantityFeatureRunner {
        override fun run(
            uid: Long?,
            categoryId: Long?,
            categoryName: String?,
            productId: Long?,
            unitId: Long?,
            amount: BigDecimal,
            price: BigDecimal,
            productPrice: BigDecimal,
            vatRate: BigDecimal?,
            quantity: Double,
            maxQuantity: Double,
            productName: String,
            barcode: String?,
            isRefund: Boolean,
            action: (Screen) -> Unit
        ) {
            this@ProductQuantityFeatureMediator.uid = uid
            this@ProductQuantityFeatureMediator.categoryId = categoryId
            this@ProductQuantityFeatureMediator.categoryName = categoryName
            this@ProductQuantityFeatureMediator.productId = productId
            this@ProductQuantityFeatureMediator.unitId = unitId
            this@ProductQuantityFeatureMediator.amount = amount
            this@ProductQuantityFeatureMediator.price = price
            this@ProductQuantityFeatureMediator.productPrice = productPrice
            this@ProductQuantityFeatureMediator.vatRate = vatRate
            this@ProductQuantityFeatureMediator.quantity = quantity
            this@ProductQuantityFeatureMediator.maxQuantity = maxQuantity
            this@ProductQuantityFeatureMediator.productName = productName
            this@ProductQuantityFeatureMediator.barcode = barcode
            this@ProductQuantityFeatureMediator.isRefund = isRefund
            action.invoke(Screens.HelperQuantity)
        }

        override fun finish(action: (ProductQuantityResult?) -> Unit): ProductQuantityFeatureRunner {
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