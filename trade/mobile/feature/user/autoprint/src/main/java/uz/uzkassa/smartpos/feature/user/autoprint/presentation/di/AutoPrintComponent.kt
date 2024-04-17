package uz.uzkassa.smartpos.feature.user.autoprint.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.autoprint.dependencies.AutoPrintFeatureDependencies
import uz.uzkassa.smartpos.feature.user.autoprint.presentation.AutoPrintFragment

@AutoPrintScope
@Component(
    dependencies = [AutoPrintFeatureDependencies::class],
    modules = [AutoPrintModule::class]
)
abstract class AutoPrintComponent {
    internal abstract fun inject(fragment: AutoPrintFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: AutoPrintFeatureDependencies
        ): AutoPrintComponent
    }

    internal companion object {

        fun create(
            dependencies: AutoPrintFeatureDependencies
        ): AutoPrintComponent =
            DaggerAutoPrintComponent
                .factory()
                .create(dependencies)
    }
}