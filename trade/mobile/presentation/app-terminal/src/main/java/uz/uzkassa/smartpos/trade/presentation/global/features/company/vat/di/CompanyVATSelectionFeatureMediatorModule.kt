package uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureArgs
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.CompanyVATSelectionFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner

@Module(
    includes = [
        CompanyVATSelectionFeatureMediatorModule.Binders::class,
        CompanyVATSelectionFeatureMediatorModule.Providers::class
    ]
)
object CompanyVATSelectionFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCompanyVATSelectionFeatureArgs(
            companyVATSelectionFeatureMediator: CompanyVATSelectionFeatureMediator
        ): CompanyVATSelectionFeatureArgs

        @Binds
        @GlobalScope
        fun bindCompanyVATSelectionFeatureCallback(
            companyVATSelectionFeatureMediator: CompanyVATSelectionFeatureMediator
        ): CompanyVATSelectionFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCompanyVATSelectionFeatureRunner(
            companyVATSelectionFeatureMediator: CompanyVATSelectionFeatureMediator
        ): CompanyVATSelectionFeatureRunner =
            companyVATSelectionFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCompanyVATSelectionFeatureMediator(): CompanyVATSelectionFeatureMediator =
            CompanyVATSelectionFeatureMediator()
    }
}