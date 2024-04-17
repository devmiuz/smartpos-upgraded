package uz.uzkassa.smartpos.feature.category.list.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.category.model.Category
import uz.uzkassa.smartpos.feature.category.list.data.CategoryListRepository
import uz.uzkassa.smartpos.feature.category.list.data.CategoryListRepositoryImpl
import uz.uzkassa.smartpos.feature.category.list.dependencies.CategoryListFeatureArgs

@Module(
    includes = [
        CategoryListModule.Binders::class,
        CategoryListModule.Providers::class
    ]
)
internal object CategoryListModule {

    @Module
    interface Binders {

        @Binds
        @CategoryListScope
        fun bindCategoryListRepository(
            impl: CategoryListRepositoryImpl
        ): CategoryListRepository
    }

    @Module
    object Providers {

        @FlowPreview
        @JvmStatic
        @Provides
        @CategoryListScope
        fun provideCategoryFlow(
            categoryListFeatureArgs: CategoryListFeatureArgs
        ): Flow<Category> =
            categoryListFeatureArgs.categoryBroadcastChannel.asFlow()
    }
}