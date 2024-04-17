package uz.uzkassa.smartpos.feature.category.selection.setup.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.category.selection.setup.data.CategorySetupRepository
import uz.uzkassa.smartpos.feature.category.selection.setup.data.CategorySetupRepositoryImpl

@Module(includes = [CategorySetupModule.Binders::class])
internal object CategorySetupModule {

    @Module
    interface Binders {

        @Binds
        @CategorySetupScope
        fun bindCategorySetupRepository(
            impl: CategorySetupRepositoryImpl
        ): CategorySetupRepository
    }
}