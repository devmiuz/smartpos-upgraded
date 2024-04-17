package uz.uzkassa.common.feature.category.selection.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.common.feature.category.selection.data.repository.CategorySelectionRepository
import uz.uzkassa.common.feature.category.selection.data.repository.CategorySelectionRepositoryImpl

@Module(includes = [CategorySelectionModule.Binders::class])
internal object CategorySelectionModule {

    @Module
    interface Binders {

        @Binds
        @CategorySelectionScope
        fun bindCategorySelectionRepository(
            impl: CategorySelectionRepositoryImpl
        ): CategorySelectionRepository
    }
}
