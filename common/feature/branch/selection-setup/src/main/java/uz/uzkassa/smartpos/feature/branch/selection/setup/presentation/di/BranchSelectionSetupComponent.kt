package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.branch.selection.setup.dependencies.BranchSelectionSetupFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.BranchSelectionSetupFragment

@BranchSelectionSetupScope
@Component(
    dependencies = [BranchSelectionSetupFeatureDependencies::class],
    modules = [BranchSelectionSetupModule::class]
)
abstract class BranchSelectionSetupComponent {

    internal abstract fun inject(fragment: BranchSelectionSetupFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: BranchSelectionSetupFeatureDependencies
        ): BranchSelectionSetupComponent
    }

    internal companion object {

        fun create(
            dependencies: BranchSelectionSetupFeatureDependencies
        ): BranchSelectionSetupComponent =
            DaggerBranchSelectionSetupComponent
                .factory()
                .create(dependencies)
    }
}