package uz.uzkassa.smartpos.trade.presentation.global.features.marking

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.feature.product_marking.data.model.ProductMarkingResult
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureArgs
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureCallback
import uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner.MarkingCameraScannerFragment
import uz.uzkassa.smartpos.feature.product_marking.presentation.scanner.MarkingScannerFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.marking.runner.ProductMarkingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit as ProductUnit

class ProductMarkingFeatureMediator : FeatureMediator, ProductMarkingFeatureCallback,
    ProductMarkingFeatureArgs {

    private var finishAction: ((ProductMarkingResult) -> Unit) by Delegates.notNull()
    private var backAction: (() -> Unit) by Delegates.notNull()
    private var openCameraAction: ((Screen) -> Unit) by Delegates.notNull()
    val featureRunner: ProductMarkingFeatureRunner = FeatureRunnerImpl()

    override var forRefund: Boolean = false
    override var uid: Long? = null
    override var categoryId: Long? = null
    override var categoryName: String? = null
    override var productId: Long? = null
    override var lastUnitId: Long? = null
    override var amount: BigDecimal by Delegates.notNull()
    override var price: BigDecimal by Delegates.notNull()
    override var productPrice: BigDecimal by Delegates.notNull()
    override var vatRate: BigDecimal? = null
    override var quantity: Double by Delegates.notNull()
    override var barcode: String? = null
    override var productName: String by Delegates.notNull()
    override var unit: ProductUnit? = null
    override var initialMarkings: Array<String> by Delegates.notNull()
    override var totalMarkings: Array<String> by Delegates.notNull()
    override var scannedMarkings: Array<String> = arrayOf()

    override fun onOpenCameraScanner(scannedMarkings: Array<String>) {
        this@ProductMarkingFeatureMediator.initialMarkings = scannedMarkings
        this@ProductMarkingFeatureMediator.scannedMarkings = scannedMarkings
        openCameraAction.invoke(Screens.ProductMarkingCameraScannerScreen)
    }

    override fun onFinish(productMarkingResult: ProductMarkingResult) {
        finishAction.invoke(productMarkingResult)
    }

    override fun onBack() {
        backAction.invoke()
    }

    private inner class FeatureRunnerImpl : ProductMarkingFeatureRunner {

        override fun back(action: () -> Unit): ProductMarkingFeatureRunner {
            backAction = action
            return this
        }

        override fun openCameraScanner(action: (Screen) -> Unit): ProductMarkingFeatureRunner {
            openCameraAction = action
            return this
        }

        override fun run(
            categoryId: Long?,
            categoryName: String?,
            productId: Long?,
            quantity: Double,
            amount: BigDecimal,
            lastUnitId: Long?,
            price: BigDecimal,
            barcode: String?,
            productName: String,
            productPrice: BigDecimal,
            uid: Long?,
            unit: ProductUnit?,
            vatRate: BigDecimal?,
            initialMarkings: Array<String>,
            totalMarkings: Array<String>,
            forRefund: Boolean,
            action: (Screen, Screen) -> Unit
        ) {
            this@ProductMarkingFeatureMediator.categoryId = categoryId
            this@ProductMarkingFeatureMediator.categoryName = categoryName
            this@ProductMarkingFeatureMediator.amount = amount
            this@ProductMarkingFeatureMediator.lastUnitId = lastUnitId
            this@ProductMarkingFeatureMediator.price = price
            this@ProductMarkingFeatureMediator.productId = productId
            this@ProductMarkingFeatureMediator.barcode = barcode
            this@ProductMarkingFeatureMediator.productName = productName
            this@ProductMarkingFeatureMediator.productPrice = productPrice
            this@ProductMarkingFeatureMediator.quantity = quantity
            this@ProductMarkingFeatureMediator.uid = uid
            this@ProductMarkingFeatureMediator.unit = unit
            this@ProductMarkingFeatureMediator.vatRate = vatRate
            this@ProductMarkingFeatureMediator.initialMarkings = initialMarkings
            this@ProductMarkingFeatureMediator.totalMarkings = totalMarkings
            this@ProductMarkingFeatureMediator.forRefund = forRefund
            this@ProductMarkingFeatureMediator.scannedMarkings = arrayOf()
            action.invoke(
                Screens.ProductMarkingScannerScreen,
                Screens.ProductMarkingCameraScannerScreen
            )
        }

        override fun finish(action: (productMarkingResult: ProductMarkingResult) -> Unit): ProductMarkingFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object ProductMarkingCameraScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment = MarkingCameraScannerFragment.newInstance()
        }

        object ProductMarkingScannerScreen : SupportAppScreen() {
            override fun getFragment(): Fragment = MarkingScannerFragment.newInstance()
        }
    }
}