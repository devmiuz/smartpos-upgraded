package uz.uzkassa.smartpos.feature.product.unit.creation.presentation.features.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.product.unit.creation.data.repository.UnitRepository
import uz.uzkassa.smartpos.feature.product.unit.creation.data.repository.UnitRepositoryImpl

@Module(includes = [ProductUnitParamsModule.Binders::class])
internal object ProductUnitParamsModule {

    @Module
    interface Binders {

        @Binds
        @ProductUnitParamsScope
        fun bindUnitRepository(
            impl: UnitRepositoryImpl
        ): UnitRepository
    }
}