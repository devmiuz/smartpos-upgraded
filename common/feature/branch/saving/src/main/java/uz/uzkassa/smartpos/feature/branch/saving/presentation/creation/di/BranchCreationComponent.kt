package uz.uzkassa.smartpos.feature.branch.saving.presentation.creation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.saving.presentation.creation.BranchCreationFragment

@BranchCreationScope
@Component(
    dependencies = [BranchSavingFeatureDependencies::class],
    modules = [BranchCreationModule::class]
)
abstract class BranchCreationComponent {

    internal abstract fun inject(fragment: BranchCreationFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: BranchSavingFeatureDependencies
        ): BranchCreationComponent
    }

    internal companion object {

        fun create(
            dependencies: BranchSavingFeatureDependencies
        ): BranchCreationComponent =
            DaggerBranchCreationComponent
                .factory()
                .create(dependencies)
    }
}
