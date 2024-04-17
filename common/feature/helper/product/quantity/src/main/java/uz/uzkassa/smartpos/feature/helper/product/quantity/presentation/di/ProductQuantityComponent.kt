package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.di

import dagger.Component
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.model.ProductQuantity
import uz.uzkassa.smartpos.feature.helper.product.quantity.dependencies.ProductQuantityFeatureDependencies
import uz.uzkassa.smartpos.feature.helper.product.quantity.domain.ProductQuantityInteractor
import uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.ProductQuantityFragment

@ProductQuantityScope
@Component(
    dependencies = [ProductQuantityFeatureDependencies::class],
    modules = [ProductQuantityModule::class]
)
abstract class ProductQuantityComponent : ProductQuantityFeatureDependencies {

    internal abstract val productQuantityInteractor: ProductQuantityInteractor

    internal abstract val productQuantityFlow: Flow<ProductQuantity>

    internal abstract fun inject(fragment: ProductQuantityFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ProductQuantityFeatureDependencies
        ): ProductQuantityComponent
    }

    internal companion object {

        fun create(dependencies: ProductQuantityFeatureDependencies): ProductQuantityComponent {

            return DaggerProductQuantityComponent
                .factory()
                .create(dependencies)
        }
    }
}