package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.services.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.service.ServiceRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.service.ServiceRepositoryImpl

@Module(includes = [ServicesModule.Binders::class])
internal object ServicesModule {

    @Module
    interface Binders {

        @Binds
        @ServicesScope
        fun bindServiceRepository(
            impl: ServiceRepositoryImpl
        ): ServiceRepository
    }
}