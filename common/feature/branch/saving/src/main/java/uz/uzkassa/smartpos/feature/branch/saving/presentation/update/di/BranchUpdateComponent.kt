package uz.uzkassa.smartpos.feature.branch.saving.presentation.update.di

import dagger.Component
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.saving.presentation.update.BranchUpdateFragment

@BranchUpdateScope
@Component(
    dependencies = [BranchSavingFeatureDependencies::class],
    modules = [BranchUpdateModule::class]
)
abstract class BranchUpdateComponent {

    internal abstract fun inject(fragment: BranchUpdateFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: BranchSavingFeatureDependencies
        ): BranchUpdateComponent
    }

    internal companion object {

        fun create(
            dependencies: BranchSavingFeatureDependencies
        ): BranchUpdateComponent =
            DaggerBranchUpdateComponent
                .factory()
                .create(dependencies)
    }
}