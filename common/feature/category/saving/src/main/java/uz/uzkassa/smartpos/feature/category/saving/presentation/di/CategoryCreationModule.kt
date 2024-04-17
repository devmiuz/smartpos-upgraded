package uz.uzkassa.smartpos.feature.category.saving.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.category.saving.data.CategorySavingRepository
import uz.uzkassa.smartpos.feature.category.saving.data.CategorySavingRepositoryImpl

@Module(includes = [CategoryCreationModule.Binders::class])
internal object CategoryCreationModule {

    @Module
    interface Binders {

        @Binds
        @CategoryCreationScope
        fun bindCategorySavingRepository(
            impl: CategorySavingRepositoryImpl
        ): CategorySavingRepository
    }
}