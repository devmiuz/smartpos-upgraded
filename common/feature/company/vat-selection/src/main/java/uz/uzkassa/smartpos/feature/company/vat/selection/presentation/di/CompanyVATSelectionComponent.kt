package uz.uzkassa.smartpos.feature.company.vat.selection.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureDependencies
import uz.uzkassa.smartpos.feature.company.vat.selection.presentation.CompanyVATSelectionFragment

@CompanyVATSelectionScope
@Component(
    dependencies = [CompanyVATSelectionFeatureDependencies::class],
    modules = [CompanyVATSelectionModule::class]
)
abstract class CompanyVATSelectionComponent {

    internal abstract fun inject(fragment: CompanyVATSelectionFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CompanyVATSelectionFeatureDependencies
        ): CompanyVATSelectionComponent
    }

    internal companion object {

        fun create(
            dependencies: CompanyVATSelectionFeatureDependencies
        ): CompanyVATSelectionComponent =
            DaggerCompanyVATSelectionComponent
                .factory()
                .create(dependencies)
    }
}