package uz.uzkassa.smartpos.feature.product.list.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.product.list.dependencies.ProductListFeatureDependencies
import uz.uzkassa.smartpos.feature.product.list.presentation.ProductListFragment

@ProductListScope
@Component(
    dependencies = [ProductListFeatureDependencies::class],
    modules = [ProductListModule::class]
)
abstract class ProductListComponent {

    internal abstract fun inject(fragment: ProductListFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ProductListFeatureDependencies
        ): ProductListComponent
    }

    internal companion object {

        fun create(
            dependencies: ProductListFeatureDependencies
        ): ProductListComponent =
            DaggerProductListComponent
                .factory()
                .create(dependencies)
    }
}