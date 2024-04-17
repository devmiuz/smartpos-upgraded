package uz.uzkassa.smartpos.feature.category.type.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.category.type.data.repository.CategoryTypeRepository
import uz.uzkassa.smartpos.feature.category.type.data.repository.CategoryTypeRepositoryImpl

@Module(includes = [CategoryTypeModule.Binders::class])
internal object CategoryTypeModule {

    @Module
    interface Binders {

        @Binds
        @CategoryTypeScope
        fun bindCategoryTypeRepository(
            impl: CategoryTypeRepositoryImpl
        ): CategoryTypeRepository
    }
}