package uz.uzkassa.smartpos.trade.presentation.global.features.product.list

import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.sendBlocking
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.utils.coroutines.channels.BroadcastChannelWrapper
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureArgs
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureCallback
import uz.uzkassa.smartpos.feature.product.list.presentation.ProductListFragment
import uz.uzkassa.smartpos.feature.product.saving.data.channel.CategorySelectionBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductUnitsBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.data.channel.ProductVATRateBroadcastChannel
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureArgs
import uz.uzkassa.smartpos.trade.presentation.global.features.product.list.runner.ProductListFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.product.saving.runner.ProductSavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import java.math.BigDecimal
import kotlin.properties.Delegates

class ProductListFeatureMediator(
    private val productSavingFeatureRunner: ProductSavingFeatureRunner,
    private val router: Router
) : FeatureMediator, ProductListFeatureArgs, ProductSavingFeatureArgs, ProductListFeatureCallback {
    private var backAction: (() -> Unit) by Delegates.notNull()
    override var branchId: Long by Delegates.notNull()
    override val categorySelectionBroadcastChannel = CategorySelectionBroadcastChannel()
    override val price: BigDecimal? = null
    override var productId: Long? = null
    override val productUnitsBroadcastChannel = ProductUnitsBroadcastChannel()
    override val productVATRateBroadcastChannel = ProductVATRateBroadcastChannel()
    override var categoryId: Long by Delegates.notNull()
    override var categoryName: String by Delegates.notNull()
    override val productBroadcastChannel = BroadcastChannelWrapper<Product>()

    val featureRunner: ProductListFeatureRunner = FeatureRunnerImpl()

    override fun onOpenProductCreation() {
        productSavingFeatureRunner
            .finish {
                productBroadcastChannel.sendBlocking(it)
            }
            .run(branchId, categoryId) { router.navigateTo(it) }
    }

    override fun onOpenProductUpdate(productId: Long, price: BigDecimal) {
        productSavingFeatureRunner
            .finish {
                productBroadcastChannel.sendBlocking(it)
            }
            .run(branchId, categoryId, productId, price) { router.navigateTo(it) }
    }

    override fun onBackFromProductList() =
        backAction.invoke()

    private inner class FeatureRunnerImpl : ProductListFeatureRunner {

        override fun run(
            branchId: Long,
            categoryId: Long,
            categoryName: String,
            action: (Screen) -> Unit
        ) {
            this@ProductListFeatureMediator.branchId = branchId
            this@ProductListFeatureMediator.categoryId = categoryId
            this@ProductListFeatureMediator.categoryName = categoryName
            action.invoke(Screens.ProductListScreen)
        }

        override fun back(action: () -> Unit): ProductListFeatureRunner {
            backAction = action
            return this
        }
    }

    private object Screens {

        object ProductListScreen : SupportAppScreen() {
            override fun getFragment(): Fragment? =
                ProductListFragment.newInstance()
        }
    }
}