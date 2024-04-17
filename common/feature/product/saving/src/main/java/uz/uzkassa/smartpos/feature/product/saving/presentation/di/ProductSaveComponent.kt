package uz.uzkassa.smartpos.feature.product.saving.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.product.saving.dependencies.ProductSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.product.saving.presentation.ProductSaveFragment

@ProductSaveScope
@Component(
    dependencies = [ProductSavingFeatureDependencies::class],
    modules = [ProductSaveModule::class]
)
abstract class ProductSaveComponent {

    internal abstract fun inject(fragment: ProductSaveFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ProductSavingFeatureDependencies
        ): ProductSaveComponent
    }

    internal companion object {

        fun create(
            dependencies: ProductSavingFeatureDependencies
        ): ProductSaveComponent =
            DaggerProductSaveComponent
                .factory()
                .create(dependencies)
    }
}