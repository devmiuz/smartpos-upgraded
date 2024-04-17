package uz.uzkassa.smartpos.trade.presentation.global.features.product.saving

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnit
import uz.uzkassa.smartpos.feature.product.saving.data.channel.CategorySelectionBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductUnitsBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductVATRateBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureCallback
import uz.uzkassa.smartpos.feature.product.saving.presentation.ProductSaveFragment
import uz.uzkassa.smartpos.trade.presentation.global.features.category.selection.runner.CategorySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.runner.ProductSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.unit.creation.runner.ProductUnitCreationFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class ProductSavingFeatureMediator(
    private val categorySelectionFeatureRunner: CategorySelectionFeatureRunner,
    private val companyVATSelectionFeatureRunner: CompanyVATSelectionFeatureRunner,
    private val productUnitCreationFeatureRunner: ProductUnitCreationFeatureRunner,
    private val router: Router
) : FeatureMediator, ProductSavingFeatureArgs, ProductSavingFeatureCallback {
    override var branchId: Long by Delegates.notNull()
    override var categoryId: Long by Delegates.notNull()
    override val categorySelectionBroadcastChannel = CategorySelectionBroadcastChannel()
    override val productUnitsBroadcastChannel = ProductUnitsBroadcastChannel()
    override val productVATRateBroadcastChannel = ProductVATRateBroadcastChannel()

    private var finishAction: ((Product) -> Unit) by Delegates.notNull()
    override var price: BigDecimal? = null
    override var productId: Long? = null

    val featureRunner: ProductSavingFeatureRunner =
        FeatureRunnerImpl()

    override fun onOpenCategorySelection() {
        categorySelectionFeatureRunner
            .finish { categorySelectionBroadcastChannel.sendBlocking(it) }
            .run(branchId) { router.navigateTo(it) }
    }

    override fun onOpenProductVATRateSelection(vatRate: BigDecimal?) {
        companyVATSelectionFeatureRunner
            .finish { productVATRateBroadcastChannel.offer(it.percent.toBigDecimal()) }
            .run(vatRate?.toDouble()) { router.navigateTo(it) }
    }

    override fun onOpenProductUnitCreation(units: List<ProductUnit>) {
        productUnitCreationFeatureRunner
            .finish { productUnitsBroadcastChannel.sendBlocking(it) }
            .run(units) { router.navigateTo(it) }
    }

    override fun onFinishProductSaving(product: Product) {
        finishAction.invoke(product)
    }

    private inner class FeatureRunnerImpl : ProductSavingFeatureRunner {

        override fun run(
            branchId: Long,
            categoryId: Long,
            productId: Long?,
            price: BigDecimal?,
            action: (Screen) -> Unit
        ) {
            this@ProductSavingFeatureMediator.branchId = branchId
            this@ProductSavingFeatureMediator.categoryId = categoryId
            this@ProductSavingFeatureMediator.price = price
            this@ProductSavingFeatureMediator.productId = productId
            action.invoke(Screens.ProductSaveScreen)
        }

        override fun finish(action: (Product) -> Unit): ProductSavingFeatureRunner {
            finishAction = action
            return this
        }
    }

    private object Screens {

        object ProductSaveScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                ProductSaveFragment.newInstance()
        }
    }
}