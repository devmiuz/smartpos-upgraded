package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.di

import dagger.Component
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.di.ProductUnitCreationComponent
import uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.ProductUnitParamsFragment

@ProductUnitParamsScope
@Component(
    dependencies = [ProductUnitCreationComponent::class],
    modules = [ProductUnitParamsModule::class]
)
abstract class ProductUnitParamsComponent {

    internal abstract fun inject(fragment: ProductUnitParamsFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            component: ProductUnitCreationComponent
        ): ProductUnitParamsComponent
    }

    internal companion object {

        fun create(
            component: ProductUnitCreationComponent
        ): ProductUnitParamsComponent =
            DaggerProductUnitParamsComponent
                .factory()
                .create(component)
    }
}