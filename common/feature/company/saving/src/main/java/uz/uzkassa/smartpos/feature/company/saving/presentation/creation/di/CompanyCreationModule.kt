package uz.uzkassa.smartpos.feature.company.saving.presentation.creation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.company.saving.data.repository.business.CompanyBusinessTypeRepository
import uz.uzkassa.smartpos.feature.company.saving.data.repository.business.CompanyBusinessTypeRepositoryImpl
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.CompanySavingRepository
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.CompanySavingRepositoryImpl
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureArgs

@Module(includes = [CompanyCreationModule.Binders::class, CompanyCreationModule.Providers::class])
internal object CompanyCreationModule {

    @Module
    interface Binders {

        @Binds
        @CompanyCreationScope
        fun bindCompanyBusinessTypeRepository(
            impl: CompanyBusinessTypeRepositoryImpl
        ): CompanyBusinessTypeRepository

        @Binds
        @CompanyCreationScope
        fun bindCompanySaveRepository(
            impl: CompanySavingRepositoryImpl
        ): CompanySavingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CompanyCreationScope
        @FlowPreview
        fun provideActivityTypesFlow(
            companySavingFeatureArgs: CompanySavingFeatureArgs
        ): Flow<List<ActivityType>> =
            companySavingFeatureArgs.activityTypesBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CompanyCreationScope
        @FlowPreview
        fun provideRegionCityPairFlow(
            companySavingFeatureArgs: CompanySavingFeatureArgs
        ): Flow<Pair<Region, City>> =
            companySavingFeatureArgs.regionCityBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CompanyCreationScope
        @FlowPreview
        fun provideCompanyVATFlow(
            companySavingFeatureArgs: CompanySavingFeatureArgs
        ): Flow<CompanyVAT> =
            companySavingFeatureArgs.companyVATBroadcastChannel.asFlow()
    }
}