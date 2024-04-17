package uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureArgs
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureCallback
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.activitytype.runner.ActivityTypeSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.CompanySavingFeatureMediator
import uz.uzkassa.smartpos.trade.presentation.global.features.company.saving.runner.CompanySavingFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.company.vat.runner.CompanyVATSelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.features.region_city.runner.RegionCitySelectionFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.global.navigation.router.GlobalRouter

@Module(
    includes = [
        CompanySavingFeatureMediatorModule.Binders::class,
        CompanySavingFeatureMediatorModule.Providers::class
    ]
)
object CompanySavingFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindCompanySavingFeatureArgs(
            companySavingFeatureMediator: CompanySavingFeatureMediator
        ): CompanySavingFeatureArgs

        @Binds
        @GlobalScope
        fun bindCompanySavingFeatureCallback(
            companySavingFeatureMediator: CompanySavingFeatureMediator
        ): CompanySavingFeatureCallback
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCompanySavingFeatureRunner(
            companySavingFeatureMediator: CompanySavingFeatureMediator
        ): CompanySavingFeatureRunner =
            companySavingFeatureMediator.featureRunner

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideCompanySavingFeatureMediator(
            activityTypeSelectionFeatureRunner: ActivityTypeSelectionFeatureRunner,
            companyVATSelectionFeatureRunner: CompanyVATSelectionFeatureRunner,
            globalRouter: GlobalRouter,
            regionCitySelectionFeatureRunner: RegionCitySelectionFeatureRunner
        ): CompanySavingFeatureMediator =
            CompanySavingFeatureMediator(
                activityTypeSelectionFeatureRunner = activityTypeSelectionFeatureRunner,
                companyVATSelectionFeatureRunner = companyVATSelectionFeatureRunner,
                regionCitySelectionFeatureRunner = regionCitySelectionFeatureRunner,
                router = globalRouter
            )
    }
}