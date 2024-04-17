package uz.uzkassa.smartpos.feature.branch.list.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.list.presentation.BranchListFragment

@BranchListScope
@Component(
    dependencies = [BranchListFeatureDependencies::class],
    modules = [BranchListModule::class]
)
abstract class BranchListComponent {

    internal abstract fun inject(fragment: BranchListFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: BranchListFeatureDependencies
        ): BranchListComponent

    }

    internal companion object {

        fun create(
            dependencies: BranchListFeatureDependencies
        ): BranchListComponent =
            DaggerBranchListComponent
                .factory()
                .create(dependencies)
    }
}
