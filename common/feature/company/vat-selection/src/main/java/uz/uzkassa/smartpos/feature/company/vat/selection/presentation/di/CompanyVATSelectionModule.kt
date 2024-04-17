package uz.uzkassa.smartpos.feature.company.vat.selection.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.company.vat.selection.data.repository.CompanyVATRepository
import uz.uzkassa.smartpos.feature.company.vat.selection.data.repository.CompanyVATRepositoryImpl

@Module(includes = [CompanyVATSelectionModule.Binders::class])
internal object CompanyVATSelectionModule {

    @Module
    interface Binders {

        @Binds
        @CompanyVATSelectionScope
        fun bindCompanyVATRepository(
            impl: CompanyVATRepositoryImpl
        ): CompanyVATRepository
    }
}