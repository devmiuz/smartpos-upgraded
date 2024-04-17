package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.di

import dagger.Component
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.product.unit.creation.data.channel.ProductUnitBroadcastChannel
import uz.uzkassa.smartpos.feature.product.unit.creation.data.model.ProductUnitDetails
import uz.uzkassa.smartpos.feature.product.unit.creation.dependencies.ProductUnitCreationFeatureDependencies
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitCreationInteractor
import uz.uzkassa.smartpos.feature.product.unit.creation.domain.ProductUnitInteractor
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.ProductUnitCreationFragment

@ProductUnitCreationScope
@Component(
    dependencies = [ProductUnitCreationFeatureDependencies::class],
    modules = [ProductUnitCreationModule::class]
)
abstract class ProductUnitCreationComponent : ProductUnitCreationFeatureDependencies {

    internal abstract fun inject(fragment: ProductUnitCreationFragment)

    internal abstract val productUnitBroadcastChannel: ProductUnitBroadcastChannel

    internal abstract val productUnitCreationInteractor: ProductUnitCreationInteractor

    internal abstract val productUnitInteractor: ProductUnitInteractor

    internal abstract val productUnitLazyFlow: Flow<List<ProductUnitDetails>>


    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ProductUnitCreationFeatureDependencies
        ): ProductUnitCreationComponent
    }

    internal companion object {

        fun create(
            dependencies: ProductUnitCreationFeatureDependencies
        ): ProductUnitCreationComponent =
            DaggerProductUnitCreationComponent
                .factory()
                .create(dependencies)
    }
}