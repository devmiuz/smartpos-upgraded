package uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner.di

import dagger.Component
import uz.uzkassa.smartpos.feature.product_marking.dependencies.ProductMarkingFeatureDependencies
import uz.uzkassa.smartpos.feature.product_marking.presentation.camera_scanner.MarkingCameraScannerFragment

@MarkingCameraScannerScope
@Component(
        dependencies = [ProductMarkingFeatureDependencies::class],
        modules = [MarkingCameraScannerModule::class]
)
abstract class MarkingCameraScannerComponent {

    internal abstract fun inject(fragment: MarkingCameraScannerFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
                dependencies: ProductMarkingFeatureDependencies
        ): MarkingCameraScannerComponent
    }

    internal companion object {

        fun create(dependencies: ProductMarkingFeatureDependencies): MarkingCameraScannerComponent {

            return DaggerMarkingCameraScannerComponent
                    .factory()
                    .create(dependencies)
        }
    }
}