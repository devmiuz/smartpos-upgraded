package uz.uzkassa.smartpos.feature.helper.product.quantity.presentation.child.quantity.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.repository.ProductUnitRepository
import uz.uzkassa.smartpos.feature.helper.product.quantity.data.repository.ProductUnitRepositoryImpl

@Module(
    includes = [
        QuantityModule.Binders::class,
        QuantityModule.Providers::class
    ]
)
internal object QuantityModule {

    @Module
    interface Binders {

        @Binds
        @QuantityScope
        fun bindProductUnitRepository(
            impl: ProductUnitRepositoryImpl
        ): ProductUnitRepository
    }

    @Module
    object Providers {

    }
}