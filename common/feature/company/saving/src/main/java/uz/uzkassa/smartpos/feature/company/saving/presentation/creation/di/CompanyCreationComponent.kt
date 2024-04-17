package uz.uzkassa.smartpos.feature.company.saving.presentation.creation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureDependencies
import uz.uzkassa.smartpos.feature.company.saving.presentation.creation.CompanyCreationFragment

@CompanyCreationScope
@Component(
    dependencies = [CompanySavingFeatureDependencies::class],
    modules = [CompanyCreationModule::class]
)
abstract class CompanyCreationComponent {

    internal abstract fun inject(fragment: CompanyCreationFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CompanySavingFeatureDependencies
        ): CompanyCreationComponent
    }

    internal companion object {

        fun create(
            dependencies: CompanySavingFeatureDependencies
        ): CompanyCreationComponent =
            DaggerCompanyCreationComponent
                .factory()
                .create(dependencies)
    }
}