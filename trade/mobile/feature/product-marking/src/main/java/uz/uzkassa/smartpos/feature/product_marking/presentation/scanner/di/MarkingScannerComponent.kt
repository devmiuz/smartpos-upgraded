package uz.uzkassa.smartpos.feature.product_marking.presentation.scanner.di

import dagger.Component
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureDependencies
import uz.uzkassa.smartpos.feature.product_marking.presentation.scanner.MarkingScannerFragment

@MarkingScannerScope
@Component(
        dependencies = [ProductMarkingFeatureDependencies::class],
        modules = [MarkingScannerModule::class]
)
abstract class MarkingScannerComponent {

    internal abstract fun inject(fragment: MarkingScannerFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
                dependencies: ProductMarkingFeatureDependencies
        ): MarkingScannerComponent
    }

    internal companion object {

        fun create(dependencies: ProductMarkingFeatureDependencies): MarkingScannerComponent {

            return DaggerMarkingScannerComponent
                    .factory()
                    .create(dependencies)
        }
    }
}