package uz.uzkassa.smartpos.feature.branch.delete.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.branch.delete.dependencies.BranchDeleteFeatureDependencies
import uz.uzkassa.smartpos.feature.branch.delete.presentation.BranchDeleteFragment

@BranchDeleteScope
@Component(
    dependencies = [BranchDeleteFeatureDependencies::class],
    modules = [BranchDeleteModule::class]
)
abstract class BranchDeleteComponent {

    internal abstract fun inject(fragment: BranchDeleteFragment)

    @Component.Factory
    internal interface Factory {

        fun create(
            component: BranchDeleteFeatureDependencies
        ): BranchDeleteComponent
    }

    internal companion object {

        fun create(
            dependencies: BranchDeleteFeatureDependencies
        ): BranchDeleteComponent =
            DaggerBranchDeleteComponent
                .factory()
                .create(dependencies)
    }
}
