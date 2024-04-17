package uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.sales.SalesDynamicsRepository
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.sales.SalesDynamicsRepositoryImpl
import uz.uzkassa.smartpos.feature.supervisior.dashboard.presentation.features.sales.delegate.di.SalesDynamicsModule.Binders

@Module(includes = [Binders::class])
internal object SalesDynamicsModule {

    @Module
    interface Binders {

        @Binds
        @SalesDynamicsScope
        fun bindSalesDynamicsRepository(
            impl: SalesDynamicsRepositoryImpl
        ): SalesDynamicsRepository
    }
}